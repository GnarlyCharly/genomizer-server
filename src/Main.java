import java.io.IOException;

import process.classes.Executor;
import process.classes.ProcessHandler;

//Class to make initial commit
public class Main {


	public static void main(String [ ] args) {
		String path = "test/male.sam";
		String bowTie = "bowtie -a -m 1 --best -p 10 -v 2 d_melanogaster_fb5_22 -q reads/MOF_male_wt_reads_sample.fastq -S " +path;
//		String[] sortSam = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.sam", "test/maleSortedSam"};
		String samToBam = "samtools-0.1.19/samtools view -bS -o test/male.bam test/male.sam";

		String[] sortBam = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.bam", "test/maleSorted"};

		//String bamToWig = "samtools pileup fileName.bam | perl -ne 'BEGIN{print "track type=wiggle_0 name=fileName description=fileName\n"};($c, $start, undef, $depth) = split; if ($c ne $lastC) { print "variableStep chrom=$c\n"; };$lastC=$c;next unless $. % 10 ==0;print "$start\t$depth\n" unless $depth<3;'  > fileName.wig"
		//samtools view -bS -o /path/*.bam /path/*.sam"
		ProcessHandler p = new ProcessHandler();
		try {
			System.out.println("nu executar vi");
			String inFile = null;
			String outFile = null;

			p.executeProcess("rawToProfile", new String[]{bowTie, samToBam}, inFile, outFile);
			System.out.println("nu har vi executat");
		} catch (IllegalArgumentException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}