/*
TOPIC:
    mysqld execută propoziția pe firul conexiunii și întoarce rezultatul; clientul, inclusiv Workbench, doar o trimite și o afișează
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    clientul pare locul în care stau tabelele; după propoziție, schemele rămân în data directory
MECHANISM:
    un fir pe client execută SELECT CONNECTION_ID(), USER(), CURRENT_USER(); rezultatul se vede la sfârșitul propoziției
LOOKALIKES:
    conexiunea deschide firul; CREATE USER scrie contul; CREATE DATABASE scrie schema; această propoziție le citește fără să le creeze
BOOK THEORY:
    Manualul 9.7: CONNECTION_ID() este unic între clienții conectați, USER() poate diferi de CURRENT_USER(), iar SELECT * FROM mysql.user dă ERROR 1044 când contul autentificat este anonim. PDF-ul descrie drumul fără o propoziție
BOOK EXAMPLE:
    Client_Server_Model.sessionOnServer
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/fast/ClientServerWorkbench_Questions.java
BREAK LAB:
    SELECT * FROM mysql.user cu CURRENT_USER() = @localhost dă ERROR 1044 pentru ''@'localhost'
TRADE-OFFS:
    schemă lipsă: CREATE DATABASE. Cont lipsă: CREATE USER. Sesiune lipsă: conexiunea
ANTI-PATTERNS:
    autorizezi după USER() sau după un script salvat doar în Workbench
STAFF QUESTIONS:
    Client_Server_Model.sessionOnServer, resultLeavesSchemaUntouched, serverThreadNotWorkbenchGrid, breakUserIsNotCurrentUser, datadirOrSwitchToCreateDatabase
NOVEL TRANSFER:
    ecranul recepției este clientul; programările confirmate rămân pe mysqld
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

1. Enunț: Cine calculează rândul din SELECT CONNECTION_ID(), USER(), CURRENT_USER() pe care îl vezi în Workbench?
   Capcană: Grila calculează numărul, deci închiderea ferestrei îl anulează.
   Cheie: mysqld îl calculează pe firul conexiunii. CONNECTION_ID() este unic între clienții conectați acum. Rezultatul se vede la sfârșitul propoziției.
2. Enunț: USER() este davida@localhost și CURRENT_USER() este @localhost. Ce cont autorizează propoziția următoare?
   Capcană: Numele tastat în client este contul cu privilegii.
   Cheie: Privilegiile urmează CURRENT_USER(). SELECT * FROM mysql.user dă atunci ERROR 1044 pentru ''@'localhost'.
3. Enunț: SQL-ul stă în editorul Workbench și nu a fost trimis. Ce există în data directory?
   Capcană: Fișierul salvat local este deja baza.
   Cheie: Data directory se schimbă doar după ce mysqld execută propoziția. Textul din editor nu are CONNECTION_ID().
4. Enunț: De ce dialogul cu host și port nu este aceeași propoziție cu SELECT CONNECTION_ID()?
   Capcană: Workbench deschis înseamnă că CONNECTION_ID() a creat sesiunea.
   Cheie: Host și port deschid firul. CONNECTION_ID() se citește după ce firul există și nu creează schema.
5. Enunț: SELECT @@datadir, CONNECTION_ID() arată calea și firul, iar schema de care ai nevoie lipsește. Ce schimbi?
   Capcană: Repeți citirea până apare baza.
   Cheie: Rămâi la citire când vrei firul sau calea. Pentru o schemă nouă treci la CREATE DATABASE. Pentru un cont nou treci la CREATE USER.

Întrebări adăugate:
*/

/*
🟢 Începător
    Ce este            lista de întrebări despre cine execută propoziția
    De ce e nevoie     fără ele, click-ul din Workbench pare mecanismul
🟡 Producție
    Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
🔴 Staff
    Unde               firul conexiunii și contul CURRENT_USER(); cheile stau scrise, corpul nu trimite SQL
Pași                   1. Citești enunțul. 2. Cheia rămâne în comentariu, nu într-o metodă.
*/
// Începător: pachetul grupă întrebările despre client și server, fără să trimită el însuși propoziția.
// Producție: package _6_Client_Server_Workbench.fast. Fără linie, întrebările nu stau lângă propoziția din Client_Server_Model.
// Expert: pachetul nu deschide firul și nu scrie în @@datadir. Sesiunea, contul și schema rămân mecanismele copiilor când ele lipsesc.
package _6_Client_Server_Workbench.fast;

/*
🟢 Începător
    Ce este            clasa de întrebări, fără o propoziție proprie
    De ce e nevoie     fără delimitare, răspunsul s-ar amesteca cu SQL-ul care chiar se trimite
🟡 Producție
    Cum se scrie       clasa nu conține SELECT; propoziția stă în Client_Server_Model.sessionOnServer
🔴 Staff
    Unde               cheile sunt în comentariul de deasupra; firul și ERROR 1044 se arată din metodele de topic
Pași                   1. Lași corpul fără metodă. 2. Închizi clasa.
*/
// Începător: clasa păstrează întrebările despre sesiune și nu calculează un CONNECTION_ID().
// Producție: ClientServerWorkbench_Questions nu ține un SELECT. Propoziția de trimis rămâne sessionOnServer.
// Expert: o metodă aici ar fi al doilea vocabular. CREATE USER și CREATE DATABASE rămân în copiii lor.
public class ClientServerWorkbench_Questions {
    // Începător: întrebările s-au oprit. Niciun rând nu a plecat spre server din această clasă.
    // Producție: acolada închide clasa fără SQL. SELECT CONNECTION_ID(), USER(), CURRENT_USER() nu se pierde din Client_Server_Model.
    // Expert: închiderea nu deconectează un fir și nu rezolvă ERROR 1044. Contul autentificat rămâne CURRENT_USER().
}
