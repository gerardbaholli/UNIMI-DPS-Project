# Progetto Sistemi Distribuiti e Pervasivi

Lo scopo del progetto è quello di realizzare un sistema di monitoraggio distribuito del livello di inquinamento atmosferico di un quartiere di una smartcity. Nel quartiere sono presenti diversi nodi ai quali sono collegati dei sensori per il rilevamento di PM10. Per motivi di privacy, questi nodi necessitano di comunicare e coordinarsi tra di loro per inviare ad un gateway della smartcity dati aggregati sul livello di inquinamento del quartiere. Questo gateway ha il compito di memorizzare questi dati aggregati e renderli accessibili ad analisti. Questi ultimi devono poter effettuare interrogazioni al gateway per ottenere le statistiche sul livello di inquinamento. L'architettura generale del sistema è illustrata [qui](https://github.com/gerardbaholli/SDP-project/blob/master/Progetto%20SDP%202020.pdf). I tre componenti principali da realizzare sono: Nodo, Gateway e Client Analista. La rete di nodi consiste in un insieme di processi che simulano i nodi del quartiere al quale sono associati i sensori di PM10. Questi processi dovranno coordinarsi tra di loro per trasmettere le varie misurazioni a Gateway. I nodi possono essere aggiunti o rimossi nella rete in maniera dinamica. Gateway è il server che ha il compito di ricevere e memorizzare i dati provenienti dai nodi. Inoltre, deve anche predisporre un sistema di monitoraggio da remoto che permetta di effettuare diversi tipi di interrogazioni sullo stato del sistema. Le interrogazioni vere e proprie vengono effettuate dagli analisti tramite l'apposito client Analista.
