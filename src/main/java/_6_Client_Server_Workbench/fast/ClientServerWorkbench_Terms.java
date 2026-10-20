/*
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

/*
🟢 Începător
    Ce este            termenii drumului dintre client și mysqld
    De ce e nevoie     fără ei, Workbench, firul și data directory par același lucru
🟡 Producție
    Cum se scrie       fiecare metodă ține propoziția termenului, gata de trimis
🔴 Staff
    Unde               fir, cont autentificat, cale @@datadir; privilegiul nu este USER()
Pași                   1. Modelul. 2. mysqld. 3. Clientul. 4. Conexiunea. 5. Data directory.
*/
// Începător: pachetul ține termenii prin care serverul execută, iar clientul doar trimite.
// Producție: package _6_Client_Server_Workbench.fast. Fără linie, termenii nu stau lângă propoziția de sesiune.
// Expert: pachetul nu este conexiunea și nu creează schema. CREATE USER rămâne copilul care scrie contul.
package _6_Client_Server_Workbench.fast;

/*
🟢 Începător
    Ce este            glosarul care predă propozițiile rolurilor din manual
    De ce e nevoie     fără metode, termenul rămâne o etichetă fără SQL
🟡 Producție
    Cum se scrie       SELECT CONNECTION_ID(), USER(), CURRENT_USER() și SELECT @@datadir
🔴 Staff
    Unde               valorile vin de la firul de pe mysqld, nu din grila Workbench
Pași                   1. Deschide clasa. 2. Fiecare termen își predă propoziția.
*/
// Începător: clasa dă câte o propoziție pentru model, server, client, conexiune și directorul de date.
// Producție: ClientServerWorkbench_Terms nu deschide host și port. Propozițiile sunt texte gata de trimis.
// Expert: clasa nu copiază CREATE DATABASE. Dacă schema lipsește, @@datadir doar arată calea.
public class ClientServerWorkbench_Terms {

    /*
    🟢 Începător
        Ce este            propoziția întregului model: fir, nume trimis, cont autentificat
        De ce e nevoie     fără ea, nu vezi că numele din client poate să nu fie contul cu drepturi
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
    🔴 Staff
        Unde               un rând de rezultat la sfârșitul propoziției; tabelele din data directory rămân neatinse
    Pași                   1. Ține propoziția. 2. O predă.
    */
    // Începător: metoda pregătește întrebarea prin care serverul își arată sesiunea și contul.
    // Producție: explainClientServerModel întoarce SELECT CONNECTION_ID(), USER(), CURRENT_USER(). Fără metodă, termenul nu are propoziție.
    // Expert: metoda nu deschide conexiunea. CONNECTION_ID() se citește pe un fir deja legat și nu ține loc de CREATE USER.
    public static String explainClientServerModel() {
        // Începător: serverul va întoarce un rând despre sesiunea acestui client. Rândurile din tabelele aplicației nu se mută în Workbench.
        // Producție: SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server. Dacă linia dispare, termenul nu mai are SQL.
        // Expert: USER() poate fi davida@localhost în timp ce CURRENT_USER() este @localhost. Linia nu creează index și nu ține lacăt pe mysql.user.
        String sql = "SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server";
        // Începător: înapoi pleacă propoziția, nu bazele din data directory.
        // Producție: return predă SELECT-ul. Fără return, termenul nu poate fi trimis la server.
        // Expert: return nu este rezultatul firului. Connector/J rămâne client: CONNECTION_ID() tot pe mysqld se calculează.
        return sql;
        // Începător: termenul modelului este pregătit. Serverul nu a primit încă textul din metodă.
        // Producție: acolada închide explainClientServerModel. Închiderea nu anulează un rezultat deja trimis de mysqld.
        // Expert: acolada nu deconectează clientul. Firul rămâne până se termină conexiunea.
    }

    /*
    🟢 Începător
        Ce este            propoziția care arată firul și locul unde serverul ține bazele
        De ce e nevoie     fără ea, Workbench pare mașina care păstrează tabelele
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id, @@datadir AS data_directory
    🔴 Staff
        Unde               mysqld ascultă și gestionează data directory; calea relativă se rezolvă la server
    Pași                   1. Ține propoziția. 2. O predă.
    */
    // Începător: metoda pregătește întrebarea „ce sesiune am și unde stau bazele”.
    // Producție: explainMysqld întoarce SELECT CONNECTION_ID(), @@datadir. Fără metodă, termenul de server nu are SQL.
    // Expert: propoziția nu pornește mysqld și nu este dialogul de conexiune. Dacă serverul nu ascultă, clientul nu primește nici fir, nici cale.
    public static String explainMysqld() {
        // Începător: serverul spune numărul sesiunii și folderul bazelor. Folderul în care ai salvat scriptul pe laptop nu este răspunsul.
        // Producție: SELECT CONNECTION_ID() AS session_id, @@datadir AS data_directory. Dacă linia dispare, nu mai arăți cine ține datele.
        // Expert: @@datadir nu alocă un index și nu scrie o schemă. O cale relativă se rezolvă în directorul curent al mysqld, nu al Workbench.
        String sql = "SELECT CONNECTION_ID() AS session_id, @@datadir AS data_directory";
        // Începător: pleacă întrebarea către server, nu un folder local.
        // Producție: return predă SELECT-ul. Fără return, termenul mysqld nu poate fi trimis.
        // Expert: return nu creează un fir. Firul îl dă managerul de conexiuni când clientul s-a putut conecta.
        return sql;
        // Începător: termenul de server este pregătit. Nicio bază nouă nu a apărut.
        // Producție: acolada închide explainMysqld. Închiderea nu mută data directory în client.
        // Expert: acolada nu oprește mysqld. Clienții deja conectați își păstrează firele.
    }

    /*
    🟢 Începător
        Ce este            propoziția care arată numele trimis de client, nu contul ales de server
        De ce e nevoie     fără ea, orice nume din fereastră pare contul cu drepturi
    🟡 Producție
        Cum se scrie       SELECT USER() AS name_from_client
    🔴 Staff
        Unde               USER() este ce a prezentat clientul; privilegiul rămâne la CURRENT_USER()
    Pași                   1. Ține propoziția. 2. O predă.
    */
    // Începător: metoda pregătește citirea numelui pe care clientul l-a trimis la autentificare.
    // Producție: explainClientProgram întoarce SELECT USER() AS name_from_client. Fără metodă, termenul de client nu are SQL.
    // Expert: Workbench, mysql și Connector/J sunt programe client. Niciunul nu gestionează data directory.
    public static String explainClientProgram() {
        // Începător: serverul întoarce numele și gazda pe care le-a primit de la client. Tabelele rămân pe server.
        // Producție: SELECT USER() AS name_from_client. Dacă linia dispare, nu mai ai propoziția care separă numele trimis de contul autentificat.
        // Expert: USER() poate rămâne davida@localhost când CURRENT_USER() este @localhost. Linia nu acordă GRANT și nu ține lacăt.
        String sql = "SELECT USER() AS name_from_client";
        // Începător: pleacă întrebarea despre numele din client, nu despre drepturile din grant tables.
        // Producție: return predă SELECT USER(). Fără return, termenul nu poate fi trimis.
        // Expert: return nu autentifică. Autentificarea a avut loc pe fir, iar contul care autorizează este CURRENT_USER().
        return sql;
        // Începător: termenul de client este pregătit. Bazele nu s-au mutat în editor.
        // Producție: acolada închide explainClientProgram. Închiderea nu schimbă contul deja autentificat.
        // Expert: acolada nu transformă Workbench în mysqld. Următoarea propoziție tot pe firul serverului se execută.
    }

    /*
    🟢 Începător
        Ce este            propoziția care citește numărul firului acestei conexiuni
        De ce e nevoie     fără ea, două ferestre Workbench par aceeași sesiune
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id
    🔴 Staff
        Unde               valoarea coincide cu Id din SHOW PROCESSLIST și este unică între clienții conectați acum
    Pași                   1. Ține propoziția. 2. O predă.
    */
    // Începător: metoda pregătește citirea numărului de sesiune dat de server acestui client.
    // Producție: explainConnection întoarce SELECT CONNECTION_ID() AS session_id. Fără metodă, termenul de conexiune nu are SQL.
    // Expert: host și port deschid firul; această propoziție îl citește după. Nu este CREATE DATABASE.
    public static String explainConnection() {
        // Începător: serverul întoarce numărul firului. Alt client conectat acum primește alt număr.
        // Producție: SELECT CONNECTION_ID() AS session_id. Dacă linia dispare, nu mai separi firul de tab-ul din Workbench.
        // Expert: schimbarea lui pseudo_thread_id pe sesiune schimbă valoarea afișată, nu data directory. La max_connections, un client fără CONNECTION_ADMIN nu primește fir, iar Connection_errors_max_connections crește.
        String sql = "SELECT CONNECTION_ID() AS session_id";
        // Începător: pleacă întrebarea despre fir, nu comanda care deschide o bază.
        // Producție: return predă SELECT CONNECTION_ID(). Fără return, termenul nu poate fi trimis.
        // Expert: return nu crește max_connections. Fără fir, propoziția nu are unde să ruleze.
        return sql;
        // Începător: termenul de conexiune este pregătit. Schema din data directory este neschimbată.
        // Producție: acolada închide explainConnection. Închiderea metodei nu închide firul.
        // Expert: firul se întoarce în thread cache doar când conexiunea se termină și cache-ul mai are loc.
    }

    /*
    🟢 Începător
        Ce este            propoziția care întreabă serverul unde stau bazele
        De ce e nevoie     fără ea, folderul scriptului pare locul tabelelor
    🟡 Producție
        Cum se scrie       SELECT @@datadir AS data_directory
    🔴 Staff
        Unde               calea este a serverului; o cale relativă se rezolvă în directorul lui curent
    Pași                   1. Ține propoziția. 2. O predă.
    */
    // Începător: metoda pregătește întrebarea despre folderul în care serverul ține bazele.
    // Producție: explainDataDirectory întoarce SELECT @@datadir AS data_directory. Fără metodă, termenul nu are SQL.
    // Expert: citirea nu este CREATE DATABASE. Schema nouă se scrie cu propoziția copilului, în acest director, doar după ce mysqld o execută.
    public static String explainDataDirectory() {
        // Începător: serverul spune calea bazelor. Un fișier .sql salvat lângă Workbench nu este această cale.
        // Producție: SELECT @@datadir AS data_directory. Dacă linia dispare, nu mai ai citirea care lasă schema necreată.
        // Expert: @@datadir nu pune un index și nu ține lacăt. Calea relativă depinde de directorul curent al mysqld la pornire, nu de directorul clientului.
        String sql = "SELECT @@datadir AS data_directory";
        // Începător: pleacă întrebarea despre loc, nu comanda care creează o bază.
        // Producție: return predă SELECT @@datadir. Fără return, termenul nu poate fi trimis.
        // Expert: return nu scrie un subdirector. Pentru o schemă lipsă treci la CREATE DATABASE; această linie doar arată unde ar ajunge.
        return sql;
        // Începător: termenul directorului de date este pregătit. Nicio bază nouă nu a fost creată.
        // Producție: acolada închide explainDataDirectory. Închiderea nu salvează scriptul în @@datadir.
        // Expert: acolada nu comută pe CREATE DATABASE. Comutarea se face după ce ai văzut că schema lipsește de pe server.
    }
    // Începător: termenii s-au oprit. Nicio bază nu a fost creată din această clasă.
    // Producție: acolada închide ClientServerWorkbench_Terms. Închiderea nu trimite SELECT-urile și nu schimbă @@datadir.
    // Expert: închiderea nu deconectează firele deja deschise. Fiecare client conectat își păstrează CONNECTION_ID()-ul.
}
