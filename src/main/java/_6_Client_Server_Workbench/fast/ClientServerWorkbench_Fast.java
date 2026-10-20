/*
TOPIC:
    mysqld execută propoziția pe firul conexiunii și întoarce rezultatul; clientul, inclusiv Workbench, doar o trimite și o afișează
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    clientul pare locul în care stau tabelele; după propoziție rămâne un rând de rezultat, iar schemele rămân în data directory
MECHANISM:
    mysqld dă clientului un fir; CONNECTION_ID() identifică firul, USER() este numele trimis de client, CURRENT_USER() este contul care autorizează
LOOKALIKES:
    conexiunea deschide firul cu host și port; această propoziție se citește după. CREATE DATABASE scrie schema, SELECT-ul de sesiune nu
BOOK THEORY:
    Manualul 9.7: mysqld gestionează data directory și ascultă clienții. CONNECTION_ID() este unic între clienții conectați. USER() poate diferi de CURRENT_USER(). PDF-ul arată drumul SQL din Workbench spre server și înapoi, fără propoziție executabilă
BOOK EXAMPLE:
    main
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/fast/ClientServerWorkbench_Fast.java
BREAK LAB:
    SELECT * FROM mysql.user când contul autentificat este anonim: ERROR 1044 pentru ''@'localhost'
TRADE-OFFS:
    schema lipsă: CREATE DATABASE. Cont lipsă: CREATE USER. Sesiune lipsă: conexiunea. Aici rămâi când firul există și vrei contul care autorizează
ANTI-PATTERNS:
    tratezi USER() ca privilegiu sau fișierul din Workbench ca data directory
STAFF QUESTIONS:
    Client_Server_Model.sessionOnServer, resultLeavesSchemaUntouched, serverThreadNotWorkbenchGrid, breakUserIsNotCurrentUser, datadirOrSwitchToCreateDatabase
NOVEL TRANSFER:
    ecranul de la recepția clinicii este clientul; programările stau pe server. Închiderea ecranului nu șterge programarea confirmată
SPACED REVIEW:
    o zi, șapte zile, treizeci de zile: fir, cont autentificat, când treci la copil

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
*/

// Începător: pachetul ține lecția scurtă a modelului client-server, lângă celelalte propoziții din același folder.
// Producție: package _6_Client_Server_Workbench.fast. Fără linie, clasa nu mai stă lângă Client_Server_Model.
// Expert: pachetul nu este o conexiune și nu pornește mysqld. Copiii conexiune, CREATE USER și CREATE DATABASE rămân în folderele lor.
package _6_Client_Server_Workbench.fast;

/*
🟢 Începător
    Ce este            exemplul minim: propoziția pe care serverul o execută pentru sesiunea acestui client
    De ce e nevoie     fără ea, rămâne doar click-ul din Workbench
🟡 Producție
    Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
🔴 Staff
    Unde               firul conexiunii; privilegiul este CURRENT_USER(), nu numele din client
Pași                   1. Ține propoziția. 2. O tipărește, gata de trimis.
*/
// Începător: clasa ține o singură propoziție, cea prin care serverul își spune firul și contul.
// Producție: declarația deschide clasa care ține SELECT CONNECTION_ID(), USER(), CURRENT_USER(). Fără ea, exemplul minim nu poate pregăti propoziția.
// Expert: clasa nu copiază CREATE USER și nu deschide Workbench. Vecinul, conexiunea, doar deschide firul pe care această propoziție se citește.
public class ClientServerWorkbench_Fast {

    /*
    🟢 Începător
        Ce este            pornirea exemplului care pregătește propoziția de sesiune
        De ce e nevoie     fără main, propoziția nu are pe unde să iasă din clasă
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
    🔴 Staff
        Unde               textul pleacă spre ieșire; mysqld îl execută doar după ce un client i-l trimite pe fir
    Pași                   1. Construiește propoziția. 2. O scrie la ieșire.
    */
    // Începător: main pregătește propoziția și o arată, ca să poată fi trimisă serverului.
    // Producție: main(String[] args) nu primește host și port. Fără semnătură, nu există intrarea exemplului.
    // Expert: args nu este dialogul de conexiune. Dacă mysqld nu are fir pentru acest client, CONNECTION_ID() nu are de unde să vină.
    public static void main(String[] args) {
        // Începător: textul întreabă serverul ce sesiune are clientul și cu ce cont l-a primit. Nicio tabelă de aplicație nu se umple.
        // Producție: SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server. Dacă linia dispare, main nu mai are propoziție.
        // Expert: USER() este numele trimis de client și poate diferi de CURRENT_USER(). Linia nu înlocuiește CREATE DATABASE și nu ține lacăt după rezultat.
        String sql = "SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server";
        // Începător: propoziția apare la ieșire, încă neexecutată de această linie.
        // Producție: System.out.println(sql) scrie SELECT-ul. Fără linie, propoziția rămâne în variabilă și nu o vezi.
        // Expert: println nu este mysqld și nu deschide socket-ul. Rezultatul cu session_id apare doar după ce un client trimite textul către server.
        System.out.println(sql);
        // Începător: exemplul s-a oprit după ce a arătat propoziția. Bazele de pe server au rămas unde erau.
        // Producție: acolada închide main. Închiderea nu retrage un rând pe care serverul l-ar fi trimis deja.
        // Expert: acolada nu deconectează un fir și nu comută pe CREATE USER. Firul se închide doar când conexiunea se termină.
    }
    // Începător: lecția scurtă s-a încheiat. Serverul încă nu a primit propoziția din această clasă.
    // Producție: acolada închide ClientServerWorkbench_Fast. Închiderea nu trimite SELECT-ul și nu scoate bazele din data directory.
    // Expert: închiderea clasei nu oprește mysqld și nu închide firul altui client. Fiecare client conectat își păstrează CONNECTION_ID()-ul propriu.
}
