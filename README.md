# MFMD
MFMD is a computational algorithm for *de novo* motif discovery. It uses heuristic approaches as a local optimizer and an algorithm based on the MEMES theory as a global optimizer.

## Getting Started
Follow the instructions below to be able to run mfmd.

### Prerequisites
Before running MFMD, you must install the following external programs: Java JRE 7 or higher, WEBLOGO and R.
For details about how to install these packages, go to:
* JAVA: www.java.com;
* WEBLOGO: http://weblogo.threeplusone.com/;
* R: https://www.r-project.org/.

After installing the R package, you must initialize the server with the following command:
```
R CMD Rserve
```

### Installing
No MFMD installation procedure is required. After downloading the [mfmd.jar](https://github.com/jadermcg/mfmd/blob/master/mfmd.jar), just run the following command line:
```
java -jar mfmd.jar <dataset_name> <motif width> <significance level>
```

## Running the tests
To test MFMD, download [mfmd.jar](https://github.com/jadermcg/mfmd/blob/master/mfmd.jar) and the [dataset_crp.fasta](https://github.com/jadermcg/mfmd/blob/master/dataset_crp.fasta) into a directory of your choice. After that, open a terminal and run:
```
Java -jar mfmd.jar dataset_crp.fasta 22 0.005
```
MFMD will create a directory called MDMF_OUT. Inside it, there will be three files:

* logo.pdf: sequence logo of motifs found;
* mfmd_out.txt: summary of the information that the algorithm discovered;
* msa.txt: fasta file with motifs found.


## Authors
* **Jader M. Caldonazzo Garbelini**
* André Yoshiaki Kashiwabara
* Danilo Sipoli Sanches

## Acknowledgments
The authors would like to thank CNPq (under grant number 458598/2014-3) for the financial support given to this
research.


