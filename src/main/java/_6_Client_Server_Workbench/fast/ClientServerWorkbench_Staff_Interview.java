/*
TOPIC:
    mysqld execută propoziția pe firul conexiunii și întoarce rezultatul; clientul, inclusiv Workbench, doar o trimite și o afișează
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    clientul pare locul în care stau tabelele; după propoziție, schemele rămân în data directory, iar privilegiul urmează CURRENT_USER()
MECHANISM:
    mysqld dă fiecărui client un fir; CONNECTION_ID() identifică firul; rezultatul se vede la sfârșitul propoziției
LOOKALIKES:
    conexiunea deschide firul; CREATE USER scrie contul; CREATE DATABASE scrie schema; SELECT CONNECTION_ID(), USER(), CURRENT_USER() nu le creează
BOOK THEORY:
    Manualul 9.7: mysqld gestionează data directory. CONNECTION_ID() este unic între clienții conectați și coincide cu Id din SHOW PROCESSLIST. USER() poate diferi de CURRENT_USER(). SELECT * FROM mysql.user dă ERROR 1044 când contul autentificat este anonim. @@datadir este calea de pe server. PDF-ul arată drumul fără propoziție
BOOK EXAMPLE:
    Client_Server_Model.sessionOnServer
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/fast/ClientServerWorkbench_Staff_Interview.java
BREAK LAB:
    SELECT * FROM mysql.user când CURRENT_USER() este @localhost: ERROR 1044 pentru ''@'localhost'
TRADE-OFFS:
    sesiune lipsă: conexiunea. Cont lipsă: CREATE USER. Schemă lipsă: CREATE DATABASE. Grila rămâne în client
ANTI-PATTERNS:
    autorizezi cu USER() sau crezi că fișierul din Workbench este data directory
STAFF QUESTIONS:
    cele 50 de mai jos; propozițiile stau în sessionOnServer, resultLeavesSchemaUntouched, serverThreadNotWorkbenchGrid, breakUserIsNotCurrentUser, datadirOrSwitchToCreateDatabase
NOVEL TRANSFER:
    la recepția unei clinici, ecranul este clientul; programarea confirmată rămâne pe mysqld după închiderea ecranului
SPACED REVIEW:
    o zi, șapte zile, treizeci de zile

Definiția unui model client-server (client-server model):
       mysqld execută propoziția și ține bazele în data directory. Clientul trimite propoziția și primește rezultatul.

Exemplu:
       ClientServerWorkbench_Terms.explainClientServerModel()
       CONNECTION_ID() vine de la firul de pe server.

Definiția unui server MySQL (mysqld):
       mysqld este programul care ascultă clienții și gestionează data directory.

Exemplu:
       ClientServerWorkbench_Terms.explainMysqld()
       @@datadir este calea de pe server.

Definiția unui program client (client program):
       Programul client trimite numele de cont și propoziția. mysql, Workbench și Connector/J sunt clienți.

Exemplu:
       ClientServerWorkbench_Terms.explainClientProgram()
       USER() păstrează numele trimis de client.

Definiția unei conexiuni (connection):
       Conexiunea este firul pe care mysqld autentifică clientul și îi execută propozițiile.

Exemplu:
       ClientServerWorkbench_Terms.explainConnection()
       CONNECTION_ID() este unic între clienții conectați acum.

Definiția unui director de date (data directory):
       Data directory este locul în care serverul ține bazele și tabelele.

Exemplu:
       ClientServerWorkbench_Terms.explainDataDirectory()
       @@datadir nu creează o schemă.

Recunoaștere din SQL, SHOW PROCESSLIST sau eroare:
1. Enunț: SELECT CONNECTION_ID() întoarce 23786. Cine a produs numărul?
   Capcană: Workbench a inventat numărul în grilă.
   Cheie: mysqld îl dă pe firul acestui client. Valoarea este de același fel cu Id din SHOW PROCESSLIST.
   Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
2. Enunț: USER() este davida@localhost și CURRENT_USER() este @localhost. Ce s-a întâmplat la autentificare?
   Capcană: Serverul a acceptat contul davida pentru că așa a scris clientul.
   Cheie: Clientul a trimis davida. Serverul a autentificat contul anonim. Privilegiile urmează CURRENT_USER().
   Demonstrație: Client_Server_Model.sessionOnServer
3. Enunț: SELECT * FROM mysql.user se încheie cu ERROR 1044 pentru ''@'localhost'. Ce cont a autorizat serverul?
   Capcană: Eroarea înseamnă că Workbench a pierdut parola din formular.
   Cheie: CURRENT_USER() este @localhost. USER() poate încă arăta numele trimis de client. Rândurile din mysql.user nu au fost citite.
   Demonstrație: Client_Server_Model.breakUserIsNotCurrentUser
4. Enunț: SHOW PROCESSLIST arată Id 23786, iar SELECT CONNECTION_ID() pe acel client dă 23786. Ce identifică numărul?
   Capcană: Numărul este id-ul ferestrei Workbench.
   Cheie: Este identificatorul firului de conexiune, unic între clienții conectați acum.
   Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
5. Enunț: Doi clienți deschiși primesc două valori CONNECTION_ID(). De ce nu sunt egale?
   Capcană: Același server înseamnă același număr de sesiune.
   Cheie: Fiecare client conectat are propriul fir. Unicitatea este între clienții conectați acum, nu un număr global etern.
   Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
6. Enunț: SELECT @@datadir întoarce o cale. Unde stau bazele?
   Capcană: Calea este folderul în care Workbench a salvat scriptul.
   Cheie: Calea este data directory al mysqld. O cale relativă se rezolvă în directorul curent al serverului.
   Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
7. Enunț: Workbench desenează grila pentru SELECT CONNECTION_ID(). A calculat grila numărul?
   Capcană: Componenta vizuală este motorul care execută SQL.
   Cheie: Grila afișează rezultatul primit. Calculul l-a făcut firul de pe mysqld.
   Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
8. Enunț: Clientul mysql și Workbench trimit același SELECT CONNECTION_ID(), USER(), CURRENT_USER(). Cine îl execută?
   Capcană: Fiecare unealtă are propriul motor de SQL.
   Cheie: Ambele sunt programe client. Execută mysqld, pe firul fiecărei conexiuni.
   Demonstrație: Client_Server_Model.sessionOnServer
9. Enunț: SELECT CONNECTION_ID(), USER(), CURRENT_USER() nu numește o tabelă de aplicație și tot întoarce un rând. De unde este rândul?
   Capcană: Lipsa unui FROM înseamnă că clientul a fabricat rândul local.
   Cheie: Rândul este rezultatul funcțiilor de sesiune, calculat pe fir, la sfârșitul propoziției. Nu este un INSERT.
   Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
10. Enunț: Serverul crește Connection_errors_max_connections și clientul nu primește sesiune. A rulat SELECT CONNECTION_ID()?
    Capcană: Propoziția a eșuat în interiorul unei tranzacții deschise.
    Cheie: Nu a existat fir pentru acel client. max_connections a refuzat conexiunea înainte de propoziție. Locul suplimentar este pentru CONNECTION_ADMIN.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid

Rânduri, index, lacăt și momentul în care se vede efectul:
11. Enunț: După SELECT CONNECTION_ID(), USER(), CURRENT_USER(), câte rânduri noi are o tabelă de programări?
    Capcană: Orice propoziție trimisă din client scrie un rând.
    Cheie: Niciunul. Propoziția întoarce un rând de rezultat și lasă tabelele din data directory neschimbate.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
12. Enunț: Când vezi session_id: la COMMIT, la propoziția următoare, sau la sfârșitul acesteia?
    Capcană: Numărul devine vizibil doar după COMMIT.
    Cheie: La sfârșitul propoziției, în client. Nu s-a deschis o scriere care să aibă nevoie de COMMIT.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
13. Enunț: După ce rezultatul a ajuns în Workbench, firul de conexiune mai există?
    Capcană: Firul moare odată cu rândul afișat.
    Cheie: Firul rămâne până se termină conexiunea. La deconectare poate intra în thread cache, dacă acesta nu este plin.
    Demonstrație: Client_Server_Model.sessionOnServer
14. Enunț: Închizi Workbench după ce mysqld a terminat propoziția. Ce se întâmplă cu rândurile din data directory?
    Capcană: Închiderea ferestrei dă înapoi ce a calculat serverul.
    Cheie: Rândurile rămân unde le ține serverul. Închiderea clientului termină firul, nu mută bazele în laptop.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
15. Enunț: SELECT @@datadir, CONNECTION_ID() a rulat. S-a creat un director nou de bază?
    Capcană: Citirea căii creează schema dacă ea lipsește.
    Cheie: Nu. @@datadir doar raportează calea. Schema nouă cere CREATE DATABASE, executat de mysqld.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
16. Enunț: Al doilea client rulează SELECT CONNECTION_ID(). Vede numărul primului client în propriul rezultat?
    Capcană: Toți clienții citesc un singur CONNECTION_ID() global.
    Cheie: Rezultatul lui este firul propriu. Firul celuilalt se vede într-un SHOW PROCESSLIST, nu în CONNECTION_ID() local.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
17. Enunț: SELECT CONNECTION_ID(), USER(), CURRENT_USER() ține un lacăt de rând pe mysql.user?
    Capcană: Orice citire despre conturi blochează grant tables până închizi clientul.
    Cheie: Propoziția nu ia lacăt de rând pe mysql.user. Eșecul de acces, când apare, este ERROR 1044, nu o așteptare de lacăt.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
18. Enunț: Setați pseudo_thread_id pe sesiune și CONNECTION_ID() se schimbă. S-a mutat data directory?
    Capcană: Noul număr înseamnă că ai preluat tabelele altui client.
    Cheie: S-a schimbat valoarea întoarsă de funcție pe sesiune. Bazele rămân în @@datadir.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
19. Enunț: După SELECT CONNECTION_ID(), câte scheme noi vezi în data directory?
    Capcană: O sesiune nouă creează o bază cu numele clientului.
    Cheie: Niciuna. Propoziția nu este CREATE DATABASE.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
20. Enunț: Lipsește un index pe tabela de programări. Se schimbă CONNECTION_ID() din această cauză?
    Capcană: Fără index, serverul nu poate aloca firul.
    Cheie: CONNECTION_ID() nu citește tabela de programări și nu depinde de indexul ei. Indexul contează la căutarea din acea tabelă, nu la identificarea firului.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid

Deosebire de mecanismul vecin:
21. Enunț: De ce dialogul cu host și port nu este SELECT CONNECTION_ID()?
    Capcană: Dacă Workbench s-a deschis, CONNECTION_ID() a creat sesiunea.
    Cheie: Host și port cer firul. CONNECTION_ID() se citește după ce firul există.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
22. Enunț: De ce CURRENT_USER() nu este CREATE USER?
    Capcană: Citirea contului îl și creează dacă lipsește.
    Cheie: CREATE USER scrie contul. CURRENT_USER() citește contul cu care serverul a autentificat deja acest fir.
    Demonstrație: Client_Server_Model.sessionOnServer
23. Enunț: De ce SELECT @@datadir nu este CREATE DATABASE?
    Capcană: Calea întoarsă înseamnă că baza din script există.
    Cheie: @@datadir arată unde stau bazele. CREATE DATABASE este propoziția care adaugă schema acolo, și doar după ce mysqld o execută.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
24. Enunț: De ce modelul client-server nu este GRANT?
    Capcană: Dacă vezi CONNECTION_ID(), contul are deja toate drepturile.
    Cheie: GRANT dă un drept. Acest SELECT spune cine execută și ce cont a fost autentificat. Dreptul rămâne al lui CURRENT_USER().
    Demonstrație: Client_Server_Model.sessionOnServer
25. Enunț: Manualul numește Workbench unealtă GUI. De ce nu este mysqld?
    Capcană: IDE-ul ține data directory pentru că acolo desenezi tabelele.
    Cheie: Workbench trimite propoziția și afișează rezultatul. mysqld ascultă, execută și gestionează data directory.
    Demonstrație: Client_Server_Model.sessionOnServer
26. Enunț: De ce Connector/J nu execută SELECT CONNECTION_ID() în interiorul JVM?
    Capcană: Driverul este un server încorporat în aplicație.
    Cheie: Connector/J este program client. Numărul îl calculează firul de pe mysqld.
    Demonstrație: Client_Server_Model.sessionOnServer
27. Enunț: De ce USER() nu este contul care decide privilegiile?
    Capcană: Numele trimis de client este suficient pentru autorizare.
    Cheie: USER() este ce a prezentat clientul. CURRENT_USER() este contul folosit la autentificare și el decide accesul.
    Demonstrație: Client_Server_Model.sessionOnServer
28. Enunț: Un fișier .sql salvat lângă proiect are CREATE DATABASE shop, netrimis. De ce shop lipsește din @@datadir?
    Capcană: Salvarea fișierului este execuția.
    Cheie: mysqld nu a primit propoziția. Data directory se schimbă doar după execuția pe server.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
29. Enunț: De ce SHOW PROCESSLIST nu înlocuiește modelul client-server?
    Capcană: Lista de procese este însăși baza de date.
    Cheie: SHOW PROCESSLIST arată firele pe care serverul le are acum. Modelul este că propoziția rulează pe unul dintre ele, iar clientul primește rezultatul.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
30. Enunț: Thread pool-ul din Enterprise schimbă cine execută SQL-ul?
    Capcană: Pool-ul mută tabelele în client ca să scadă numărul de fire.
    Cheie: Schimbă modul în care serverul repartizează execuția pentru mulți clienți. Data directory rămâne la mysqld.
    Demonstrație: Client_Server_Model.sessionOnServer

Eșec în producție:
31. Enunț: Aplicația permite acțiunea dacă USER() conține davida, iar CURRENT_USER() este @localhost. Ce drepturi se aplică pe server?
    Capcană: Verificarea din ecran ține loc de contul autentificat.
    Cheie: Serverul aplică privilegiile lui @localhost. Următorul SELECT * FROM mysql.user poate lua ERROR 1044.
    Demonstrație: Client_Server_Model.breakUserIsNotCurrentUser
32. Enunț: SELECT * FROM mysql.user dă ERROR 1044. Parola tastată în client a creat contul davida?
    Capcană: Orice parolă trimisă de client creează contul în grant tables.
    Cheie: Nu. Parola a însoțit numele trimis. Contul cu care serverul a autentificat poate fi anonim, iar CREATE USER este alt mecanism.
    Demonstrație: Client_Server_Model.breakUserIsNotCurrentUser
33. Enunț: mysqld nu ascultă. Poate Workbench afișa un CONNECTION_ID() proaspăt?
    Capcană: Ultima grilă salvată este sesiunea curentă.
    Cheie: Fără server pornit, clientul nu capătă fir. Nu există un CONNECTION_ID() nou.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
34. Enunț: max_connections este atins. Contul fără CONNECTION_ADMIN trimite SELECT CONNECTION_ID(). Unde se oprește?
    Capcană: Propoziția intră într-o coadă și ține lacăt până se eliberează un fir.
    Cheie: Conexiunea este refuzată, Connection_errors_max_connections crește, iar propoziția nu are fir. Locul suplimentar este max_connections + 1 pentru CONNECTION_ADMIN.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
35. Enunț: Doi oameni deschid același fișier SQL și se așteaptă la același CONNECTION_ID(). Ce primește fiecare?
    Capcană: Fișierul comun este o sesiune comună.
    Cheie: Fiecare conexiune are propriul fir și propriul CONNECTION_ID() între clienții conectați acum.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
36. Enunț: @@datadir este o cale relativă. Clientul de pe alt laptop citește folderul lui curent. Ce director raportează serverul?
    Capcană: Calea relativă se rezolvă pe mașina unde rulează Workbench.
    Cheie: Se rezolvă față de directorul curent al mysqld. Clientul nu mută data directory la el.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
37. Enunț: Clientul se deconectează și firul intră în thread cache. Dispare un rând deja scris de o propoziție anterioară, confirmată?
    Capcană: Cache-ul de fire păstrează și rândurile, deci deconectarea le șterge.
    Cheie: În cache se întoarce firul inactiv, dacă mai este loc. Rândurile confirmate rămân în data directory.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched
38. Enunț: O vedere fără SQL SECURITY INVOKER întoarce CURRENT_USER() al definitorului, nu numele din Workbench. Cine autorizează interogarea din vedere?
    Capcană: Omul care a deschis grila este întotdeauna contul verificat.
    Cheie: Pentru acea vedere, CURRENT_USER() este contul DEFINER. USER() rămâne numele trimis de client.
    Demonstrație: Client_Server_Model.sessionOnServer
39. Enunț: SQL-ul este scris în editor și execuția nu a fost cerută. SHOW DATABASES de pe alt client nu arată baza. Unde a rămas textul?
    Capcană: Editorul partajează data directory între clienți.
    Cheie: Textul a rămas în client. mysqld nu l-a executat, deci data directory nu are schema.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
40. Enunț: Schimbi pseudo_thread_id și crezi că ai preluat sesiunea altui client. Ce arată în continuare SHOW PROCESSLIST pentru firul tău?
    Capcană: Funcția și firul real sunt același obiect, deci ai luat conexiunea celuilalt.
    Cheie: S-a schimbat valoarea întoarsă de CONNECTION_ID() pe sesiunea ta. Nu ai devenit firul celuilalt client și nu i-ai luat tabelele.
    Demonstrație: Client_Server_Model.resultLeavesSchemaUntouched

Trade-off, inclusiv când rămâi în aplicație:
41. Enunț: Sesiunea există și vrei contul care va autoriza propoziția următoare. Ce trimiți?
    Capcană: Deschizi din nou dialogul de conexiune ca să „reîncarci” privilegiile.
    Cheie: Rămâi la SELECT CURRENT_USER(). Dialogul de conexiune este pentru un fir care încă nu există.
    Demonstrație: Client_Server_Model.sessionOnServer
42. Enunț: Schema de care ai nevoie lipsește din @@datadir. Ce propoziție lași?
    Capcană: Mai trimiți o dată SELECT CONNECTION_ID() până apare baza.
    Cheie: Lași citirea de cale. Treci la CREATE DATABASE, executat de mysqld în data directory.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
43. Enunț: USER() arată un nume care nu există în grant tables. Ce mecanism creează contul?
    Capcană: Repeți SELECT USER() după ce ai tastat parola în Workbench.
    Cheie: CREATE USER scrie contul. USER() doar raportează numele trimis pe firul deja deschis.
    Demonstrație: Client_Server_Model.sessionOnServer
44. Enunț: Vrei culorile grilei și filtrarea vizuală a rezultatului. Unde le pui, și unde lași decizia de acces?
    Capcană: Mut tot controlul de acces în Workbench, lângă culori.
    Cheie: Culorile rămân în client. Accesul rămâne la CURRENT_USER() pe server.
    Demonstrație: Client_Server_Model.sessionOnServer
45. Enunț: Aceeași propoziție trebuie să plece dintr-o aplicație Java. Ce este Connector/J în acest drum?
    Capcană: Driverul execută SQL în JVM și ține o copie a data directory.
    Cheie: Este alt program client. mysqld execută în continuare SELECT CONNECTION_ID(), USER(), CURRENT_USER().
    Demonstrație: Client_Server_Model.sessionOnServer
46. Enunț: max_connections este plin și un administrator trebuie să vadă firele. Ce cont poate încă intra?
    Capcană: Deschizi o a doua bază de date ca să ocolești limita.
    Cheie: Un cont cu CONNECTION_ADMIN poate folosi conexiunea suplimentară max_connections + 1 și SHOW PROCESSLIST. Limita nu se ocolește cu un alt @@datadir.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
47. Enunț: Vrei să vezi toți clienții conectați acum. Lista de taburi din Workbench ajunge?
    Capcană: Taburile locale sunt procesele serverului.
    Cheie: SHOW PROCESSLIST pe server arată firele. Taburile sunt doar clientul acesta.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
48. Enunț: Vrei calea fișierelor de baze. Folderul proiectului din IDE este răspunsul?
    Capcană: Proiectul Maven este data directory.
    Cheie: SELECT @@datadir pe server. Folderul proiectului ține codul clientului, nu bazele.
    Demonstrație: Client_Server_Model.datadirOrSwitchToCreateDatabase
49. Enunț: CURRENT_USER() este @localhost și ai nevoie de un cont cu nume. Ce repari?
    Capcană: Recalculezi grila sau schimbi tema din Workbench.
    Cheie: Repari contul de pe server, cu CREATE USER sau cu contul deja existent. SELECT USER() nu scrie grant tables.
    Demonstrație: Client_Server_Model.breakUserIsNotCurrentUser
50. Enunț: Vrei mai mulți clienți de probă asupra aceluiași mysqld. Mai multe ferestre Workbench țin datele local?
    Capcană: Fiecare fereastră are propria bază, deci încărcarea nu atinge serverul.
    Cheie: Fiecare fereastră este un client și primește un fir. Datele rămân în data directory. mysqlslap emulează tot clienți, nu un al doilea server.
    Demonstrație: Client_Server_Model.serverThreadNotWorkbenchGrid
*/

/*
🟢 Începător
    Ce este            banca de interviu despre cine execută propoziția
    De ce e nevoie     fără ea, răspunsul se oprește la click-ul din Workbench
🟡 Producție
    Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
🔴 Staff
    Unde               fir, ERROR 1044, @@datadir; cheile sunt scrise, clasa nu trimite un al doilea SQL
Pași                   1. Citești enunțul. 2. Cheia numește firul, contul sau calea.
*/
// Începător: pachetul ține întrebările despre sesiunea de pe server, nu despre click-ul din editor.
// Producție: package _6_Client_Server_Workbench.fast. Fără linie, banca nu stă lângă propoziția din Client_Server_Model.
// Expert: pachetul nu deschide host și port și nu scrie schema. CREATE USER și CREATE DATABASE rămân copiii la care treci doar când contul sau schema lipsesc.
package _6_Client_Server_Workbench.fast;

/*
🟢 Începător
    Ce este            cincizeci de întrebări despre fir, cont și data directory
    De ce e nevoie     fără clasă, cheile nu au unde sta lângă propoziția de sesiune
🟡 Producție
    Cum se scrie       propozițiile rămân în metodele din Client_Server_Model; aici nu este un main
🔴 Staff
    Unde               recunoaștere, efect, vecin, eșec, alegere; niciuna nu se mută pe WHERE sau pe JOIN
Pași                   1. Ține cheile în comentariu. 2. Închide clasa fără să trimiți SQL.
*/
// Începător: clasa păstrează întrebările și nu calculează un CONNECTION_ID() al ei.
// Producție: ClientServerWorkbench_Staff_Interview nu are main și nu ține un SELECT nou. Propoziția de trimis rămâne sessionOnServer.
// Expert: un SQL copiat aici ar dubla demo-ul. Vecinii rămân conexiunea, CREATE USER și CREATE DATABASE.
public class ClientServerWorkbench_Staff_Interview {
    // Începător: banca s-a încheiat. Niciun rând nu a plecat spre mysqld din această clasă.
    // Producție: acolada închide clasa fără propoziție. SELECT CONNECTION_ID(), USER(), CURRENT_USER() rămâne în Client_Server_Model.
    // Expert: închiderea nu deconectează un fir și nu repară ERROR 1044. Contul care autorizează rămâne CURRENT_USER().
}
