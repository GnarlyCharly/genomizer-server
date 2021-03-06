package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.constants.ServerDependentValues;
import database.containers.Genome;
import database.testSuite.TestInitializer;

public class GenomeReleaseTableTests {
    public static TestInitializer ti;
    public static DatabaseAccessor dbac;
    public static FilePathGenerator fpg;

    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";
    private static File testFolder;
    private static String testFolderPath;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setupWithoutAddingTuples();
        fpg = dbac.getFilePathGenerator();

        testFolderPath = System.getProperty("user.home") + File.separator
                + testFolderName + File.separator;

        testFolder = new File(testFolderPath);

        if (!testFolder.exists()) {
            testFolder.mkdirs();
        }

        fpg.setRootDirectory(testFolderPath);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        recursiveDelete(testFolder);
    }

    @Before
    public void setUp() throws Exception {
        ti.addTuples();
    }

    @After
    public void tearDown() throws Exception {
        ti.removeTuplesKeepConnection();
    }

    @Test
    public void testGetDeleteGetAddGet() throws Exception {
        Genome g = dbac.getGenomeRelease("hg19");
        assertEquals("hg19", g.genomeVersion);

        dbac.removeGenomeRelease("hg19");
        g = dbac.getGenomeRelease("hg19");
        assertNull(g);

        dbac.addGenomeRelease("hg19", "Human", "hg19.txt");
        g = dbac.getGenomeRelease("hg19");
        assertEquals("hg19", g.genomeVersion);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenAddFileAlreadyExist()
    									throws Exception {

    	dbac.addGenomeRelease("test12", "Bear", "test12.txt");
    	dbac.addGenomeRelease("test12", "Bear", "test12.txt");
    }

    @Test
    public void shouldReturnNullWhenGenomeReleaseDontExist()
    									throws Exception {

    	assertNull(dbac.getGenomeRelease("pangabanga"));

    }

    @Test
    public void shouldReturnRightNamesOfGenomeVersions() throws Exception {
        List<Genome> genomeList = dbac.getAllGenomReleasesForSpecies("Human");

        assertEquals(3, genomeList.size());
        assertTrue(searchGenomeForVersion(genomeList, "hg38"));
        assertTrue(searchGenomeForVersion(genomeList, "hg19"));
        assertTrue(searchGenomeForVersion(genomeList, "hg18"));

    }

    @Test
    public void shouldReturnSpecificGenomeVersionFolderPath() throws Exception {

        Genome genome = dbac.getGenomeRelease("hg38");
        assertEquals("/var/www/data/genome_releases/Human/hg38/",
                genome.folderPath); // From add_test_tuples.sql
    }

    @Test
    public void shouldReturnUploadURLUponAdd() throws Exception {
        String uploadURL = dbac.addGenomeRelease("hg39", "Human", "hg39.fasta");
        String expectedUploadURL = ServerDependentValues.UploadURL
                + fpg.generateGenomeReleaseFolder("hg39", "Human")
                + "hg39.fasta";
        assertEquals(expectedUploadURL, uploadURL);
    }

    @Test (expected = IOException.class)
    public void shouldThrowIOExceptionWhenAttemptingToAddDuplicateFile() throws Exception {
        dbac.addGenomeRelease("hg38", "Human", "hg38.fasta");
    }

    @Test
    public void shouldUpdateDatabaseUponAdd() throws Exception {
        dbac.addGenomeRelease("hg40", "Human", "hg40.fasta");
        String expectedFolderPath = fpg.getGenomeReleaseFolderPath("hg40",
                "Human");
        Genome genome = dbac.getGenomeRelease("hg40");
        assertEquals(expectedFolderPath, genome.folderPath);
    }

    @Test
    public void shouldReturnFileName() throws Exception {
        dbac.addGenomeRelease("rn50", "Rat", "aRatFile.fasta");
        Genome genome = dbac.getGenomeRelease("rn50");

        assertEquals(1, genome.getFiles().size());
        assertEquals("aRatFile.fasta", genome.getFiles().get(0));
        assertEquals(genome.species, "Rat");
        assertEquals(genome.genomeVersion, "rn50");

    }

    @Test
    public void shouldDeleteFromBothDatabaseAndFileSystem() throws Exception {
        dbac.addGenomeRelease("hg41", "Human", "hg41.txt");

        String genomeReleaseFolderPath = fpg.generateGenomeReleaseFolder(
                "hg41", "Human");
        addMockFile(genomeReleaseFolderPath, "hg41.txt");

        File genomeReleaseFolder = new File(genomeReleaseFolderPath);
        assertTrue(genomeReleaseFolder.exists());

        File genomeReleaseFile = new File(genomeReleaseFolderPath + "hg41.txt");
        assertTrue(genomeReleaseFile.exists());

        dbac.removeGenomeRelease("hg41");

        Genome g = dbac.getGenomeRelease("hg41");
        assertNull(g);

        assertFalse(genomeReleaseFolder.exists());
    }

    @Test(expected = IOException.class)
    public void shouldNotDeleteGenomReleaseWhenFileNeedsIt() throws Exception{
    	dbac.removeGenomeRelease("hg38");
    }

    @Test
    public void shouldBeAbleToGetDownloadURLs() throws Exception {
        Genome g = dbac.getGenomeRelease("hg38");
        assertEquals(2, g.getFiles().size());
        String downloadURL = getDownloadURL(g, "hg38.fasta");
        assertEquals(ServerDependentValues.DownloadURL + g.folderPath + "hg38.fasta", downloadURL);
    }

    private String getDownloadURL(Genome g, String string) {
        for (String s: g.getDownloadURLs()) {
            if (s.endsWith(string)) {
                return s;
            }
        }
        return null;
    }

    @Test
    public void shouldBeAbleToGetAllSpeciesThatHaveAGenomeRelease() throws Exception {
        List<String> species = dbac.getAllGenomReleaseSpecies();
        assertEquals(2, species.size());
    }

    @Test
    public void shouldBeAbleToGetAllGenomeReleases() throws Exception {
        List<Genome> genomes = dbac.getAllGenomReleases();
        assertEquals(6, genomes.size());
    }

    @Test
    public void shouldGetFilePrefix() throws Exception {
        Genome g = dbac.getGenomeRelease("hg18");
        assertEquals("hg18", g.getFilePrefix());
    }

    @Test
    public void shouldGetFilePrefixForComplexFileNames() throws Exception {
        dbac.addGenomeRelease("rua888", "Superhero", "superheroRua888.ping.pong");
        Genome g = dbac.getGenomeRelease("rua888");
        assertEquals("superheroRua888", g.getFilePrefix());
    }

    @Test
    public void shouldReturnEmptyListFromGetAllGenomeReleasesWhenTableIsEmpty() throws Exception{

    	tearDown();

    	assertEquals(0, dbac.getAllGenomReleases().size());
    }


    private boolean searchGenomeForVersion(List<Genome> genomeList,
            String version) {

        for (int i = 0; i < genomeList.size(); i++) {
            if (genomeList.get(i).genomeVersion.equals(version)) {
                return true;
            }
        }

        return false;
    }

    private static void addMockFile(String folderPath, String filename1)
            throws IOException {
        File file1 = new File(folderPath + filename1);
        file1.createNewFile();
    }

    private static void recursiveDelete(File folder) {
        File[] contents = folder.listFiles();
        if (contents == null || contents.length == 0) {
            folder.delete();
        } else {
            for (File f : contents) {
                recursiveDelete(f);
            }
        }
        folder.delete();
    }
}
