package geo.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import geo.GEOFileTuple;
import geo.TXTParser;

import org.junit.Before;
import org.junit.Test;

public class TXTParserTest {

	private String path = "src/geo/test/GSE47236_series_matrix.txt";
	private ArrayList<GEOFileTuple> gl;

	@Before
	public void setUp() {
		try {
			gl = TXTParser.readFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReceiveList() {
		try {
			assertNotNull(TXTParser.readFile(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFirstSeriesRelation() {
		assertTrue(gl.get(0).seriesRelation
				.equals("SRA: http://www.ncbi.nlm.nih.gov/sra?term=SRP023360"));
	}

	@Test
	public void testFirstSeriesStatus() {
		assertTrue(gl.get(0).seriesStatus.equals("Public on May 28 2013"));
	}

	@Test
	public void testFirstSeriesContributor1() {
		assertTrue(gl.get(0).seriesContributors[0].equals("Gary Karpen"));
	}

	@Test
	public void testFirstSeriesContributor2() {
		assertTrue(gl.get(0).seriesContributors[1].equals("Sarah Elgin"));
	}

	@Test
	public void testFirstSeriesContributorLast() {
		assertTrue(gl.get(0).seriesContributors[13].equals("Cameron Kennedy"));
	}

	@Test
	public void testValueIsNotNull() {
		assertNotNull(gl.get(0).seriesRelation);
		assertNotNull(gl.get(0).seriesSampleTaxid);
		assertNotNull(gl.get(0).seriesPlatformTaxid);
		assertNotNull(gl.get(0).seriesPlatformId);
		assertNotNull(gl.get(0).seriesContactCountry);
		assertNotNull(gl.get(0).seriesContactPostalCode);
		assertNotNull(gl.get(0).seriesContactState);
		assertNotNull(gl.get(0).seriesContactCity);
		assertNotNull(gl.get(0).seriesContactAddress);
		assertNotNull(gl.get(0).seriesContactInstitute);
		assertNotNull(gl.get(0).seriesContactLaboratory);
		assertNotNull(gl.get(0).seriesContactPhone);
		assertNotNull(gl.get(0).seriesContactEmail);
		assertNotNull(gl.get(0).seriesContactName);
		assertNotNull(gl.get(0).seriesSampleId);
		assertNotNull(gl.get(0).seriesContributors[0]);
		assertNotNull(gl.get(0).seriesType);
		assertNotNull(gl.get(0).seriesOverallDesign);
		assertNotNull(gl.get(0).seriesSummary[0]);
		assertNotNull(gl.get(0).seriesWebLink);
		assertNotNull(gl.get(0).seriesLastUpdateDate);
		assertNotNull(gl.get(0).seriesSubmissionDate);
		assertNotNull(gl.get(0).seriesStatus);
		assertNotNull(gl.get(0).seriesGeoAccession);
		assertNotNull(gl.get(0).seriesTitle);
	}

	@Test
	public void testValueIsNull() {
		assertNull(gl.get(0).ID_REF);
		assertNull(gl.get(0).sampleSupplementaryFile);
		assertNull(gl.get(0).sampleRelation);
		assertNull(gl.get(0).sampleLibraryStrategy);
		assertNull(gl.get(0).sampleLibrarySource);
		assertNull(gl.get(0).sampleLibrarySelection);
		assertNull(gl.get(0).sampleInstrumentModel);
		assertNull(gl.get(0).sampleContactPostalCode);
		assertNull(gl.get(0).sampleContactState);
		assertNull(gl.get(0).sampleContactCity);
		assertNull(gl.get(0).sampleContactAddress);
		assertNull(gl.get(0).sampleContactInstitute);
		assertNull(gl.get(0).sampleContactPhone);
		assertNull(gl.get(0).sampleContactLaboratory);
		assertNull(gl.get(0).sampleContactEmail);
		assertNull(gl.get(0).sampleContactName);
		assertNull(gl.get(0).samplePlatformId);
		assertNull(gl.get(0).sampleDataProcessing);
		assertNull(gl.get(0).sampleDescription);
		assertNull(gl.get(0).sampleTaxid);
		assertNull(gl.get(0).sampleExtractProtocol);
		assertNull(gl.get(0).sampleMolecule);
		assertNull(gl.get(0).sampleCharacteristics);
		assertNull(gl.get(0).sampleOrganism);
		assertNull(gl.get(0).sampleSourceName);
		assertNull(gl.get(0).sampleChannelCount);
		assertNull(gl.get(0).sampleType);
		assertNull(gl.get(0).sampleLastUpdateDate);
		assertNull(gl.get(0).sampleSubmissionDate);
		assertNull(gl.get(0).sampleStatus);
		assertNull(gl.get(0).sampleGeoAccession);
		assertNull(gl.get(0).sampleTitle);
		assertNull(gl.get(0).sampleSupplementaryFile);
	}

	@Test
	public void returnFiveObjects() {
		assertEquals(gl.size(), 5);
	}

	@Test
	public void testSampleGEOFileTupleNotNull() {
		assertNotNull(gl.get(1).ID_REF);
		assertNotNull(gl.get(1).sampleSupplementaryFile);
		assertNotNull(gl.get(1).sampleRelation);
		assertNotNull(gl.get(1).sampleLibraryStrategy);
		assertNotNull(gl.get(1).sampleLibrarySource);
		assertNotNull(gl.get(1).sampleLibrarySelection);
		assertNotNull(gl.get(1).sampleInstrumentModel);
		assertNotNull(gl.get(1).sampleContactPostalCode);
		assertNotNull(gl.get(1).sampleContactState);
		assertNotNull(gl.get(1).sampleContactCity);
		assertNotNull(gl.get(1).sampleContactAddress);
		assertNotNull(gl.get(1).sampleContactInstitute);
		assertNotNull(gl.get(1).sampleContactPhone);
		assertNotNull(gl.get(1).sampleContactLaboratory);
		assertNotNull(gl.get(1).sampleContactEmail);
		assertNotNull(gl.get(1).sampleContactName);
		assertNotNull(gl.get(1).samplePlatformId);
		assertNotNull(gl.get(1).sampleDataProcessing);
		assertNotNull(gl.get(1).sampleDescription);
		assertNotNull(gl.get(1).sampleTaxid);
		assertNotNull(gl.get(1).sampleExtractProtocol);
		assertNotNull(gl.get(1).sampleMolecule);
		assertNotNull(gl.get(1).sampleCharacteristics);
		assertNotNull(gl.get(1).sampleOrganism);
		assertNotNull(gl.get(1).sampleSourceName);
		assertNotNull(gl.get(1).sampleChannelCount);
		assertNotNull(gl.get(1).sampleType);
		assertNotNull(gl.get(1).sampleLastUpdateDate);
		assertNotNull(gl.get(1).sampleSubmissionDate);
		assertNotNull(gl.get(1).sampleStatus);
		assertNotNull(gl.get(1).sampleGeoAccession);
		assertNotNull(gl.get(1).sampleTitle);
		assertNotNull(gl.get(1).sampleSupplementaryFile);
	}

	@Test
	public void testGetURLToSRA() {
		String url = "ftp://ftp-trace.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByExp/sra/SRX/SRX287/SRX287594";
		try {
			String fullURL = TXTParser.getSRAFromDir(url);
			assertEquals(fullURL, url + "/SRR869737/SRR869737.sra");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
