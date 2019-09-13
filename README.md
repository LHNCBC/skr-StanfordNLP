README

Dongwook Shin, NLM
July 2019

This package makes the Stanford CoreNLP 3.3.1 run as a server and adds two clients programs, one of which calls the server with input and output as a command line, the other calling the server through the NLM Scheduler.

When you run this through NLM Scheduler, you need to have a UMLS liscense. 

The Scheduler command for Stanford Core NLP is "sf_parser", which contains the following script:

#!/bin/bash
SF=/nfsvol/nls/specialist/StanfordNLP
CP=${SF}/resources
CP=${CP}:${SF}/resources/factuality_semrep.properties
CP=${CP}:${SF}/lib/collections-generic-4.01.jar
CP=${CP}:${SF}/lib/jaws-bin.jar
CP=${CP}:${SF}/lib/jgrapht-core-0.9.0.jar
CP=${CP}:${SF}/lib/nlp.jar
CP=${CP}:${SF}/lib/stanford-corenlp-3.3.1-models.jar
CP=${CP}:${SF}/lib/stanford-corenlp-3.3.1.jar
CP=${CP}:${SF}/lib/xom.jar 
echo ${CP}
java -cp ${CP} gov.nih.nlm.ling.wrappers.NLPSchedulerClient $1 $2 $3


The CoreNLP server is runnig on a Linux server using the script nlpProcess.sh as:

java -Xmx128g -cp /nfsvol/nls/specialist/StanfordNLP/lib/collections-generic-4.01.jar:/nfsvol/nls/specialist/StanfordNLP/lib/jaws-bin.jar:/nfsvol/nls/specialist/StanfordNLP/lib/jgrapht-core-0.9.0.jar:/nfsvol/nls/specialist/StanfordNLP/lib/nlp.jar:/nfsvol/nls/specialist/StanfordNLP/lib/stanford-corenlp-3.3.1-models.jar:/nfsvol/nls/specialist/StanfordNLP/lib/stanford-corenlp-3.3.1.jar:/nfsvol/nls/specialist/StanfordNLP/lib/xom.jar gov.nih.nlm.ling.wrappers.CoreNLPProcess 22222

So, when a job is submitted to NLM Scheduler lloking for "sf_parser", the scheduler runs gov.nih.nlm.ling.wrappers.NLPSchedulerClient with the given input file and calls the server that runs the Stanford Core NLP and retrievs the result.
 
* In order to run it, you need to add the Stanford Core NLP model file, stanford-corenlp-3.3.1-models.jar to lib directory.
