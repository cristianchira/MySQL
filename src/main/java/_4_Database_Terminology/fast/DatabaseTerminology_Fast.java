// Începător: fișierul ține propoziția prin care vezi ce componentă poate păstra rândurile unei tabele.
// Producție: pachetul _4_Database_Terminology.fast este locul lecției. Dacă linia dispare, chooseEngine nu mai intră în compilarea acestui arbore.
// Expert: pachetul nu leagă tabela de InnoDB. Propoziția este SHOW ENGINES. MyISAM rămâne vecinul cu Transactions NO și cu lacăt de tabelă.
package _4_Database_Terminology.fast;

/*
TOPIC:
    Motorul de stocare numit de ENGINE, sau InnoDB când clauza lipsește pe MySQL 9.7, este componenta care execută schimbarea de rânduri pe acea tabelă și care decide dacă ROLLBACK mai poate scoate rândul.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Problema este să numești schema, diagrama sau încărcarea și să crezi că rândul deja stă într-o tranzacție. După propoziție, tabela este legată de o componentă: la InnoDB schimbarea încă deschisă poate fi dată înapoi; la un motor cu Transactions NO rândul rămâne.
MECHANISM:
    Serverul citește ENGINE sau motorul implicit și încarcă acea componentă. Componenta execută INSERT, UPDATE și DELETE. Definirea tabelei se vede la sfârșitul lui CREATE TABLE sau ALTER TABLE. SHOW ENGINES nu schimbă rânduri: arată Support și Transactions înainte de scriere.
LOOKALIKES:
    MyISAM. Îi separă rezultatul SHOW ENGINES: InnoDB are Support DEFAULT, Transactions YES și lacăt de rând; MyISAM are Transactions NO și lacăt de tabelă. Cuvântul OLTP, depozitul de date sau desenul din Workbench nu este clauza ENGINE.
BOOK THEORY:
    Pe MySQL 9.7, storage engine este componenta care execută propoziția pentru un tip de tabelă. InnoDB este implicit și un CREATE TABLE fără ENGINE creează o tabelă InnoDB, cu commit, rollback, recuperare după oprire, lacăt de rând, index grupat pe cheia primară și FOREIGN KEY. SHOW ENGINES listează Support YES, NO sau DEFAULT și Transactions. MyISAM are Transactions NO și lacăt de tabelă. ENGINE pe CREATE TABLE sau ALTER TABLE schimbă componenta. Dacă motorul cerut nu este disponibil, serverul creează tabela cu motorul implicit și avertizează; cu NO_ENGINE_SUBSTITUTION dă eroare și nu creează tabela. Poți folosi motoare diferite în aceeași schemă. PDF-ul SQL+Keywords.pdf deschis din acest folder este o listă de cuvinte cheie, începând cu ADD, și nu descrie motorul.
BOOK EXAMPLE:
    DatabaseTerminology_Fast.chooseEngine
REPO DEMO:
    src/main/java/_4_Database_Terminology/fast/DatabaseTerminology_Fast.java
BREAK LAB:
    CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING. Fără NO_ENGINE_SUBSTITUTION apare un avertisment și tabela este creată cu motorul implicit. Cu NO_ENGINE_SUBSTITUTION serverul dă eroare și tabela nu este creată.
TRADE-OFFS:
    MyISAM pentru o tabelă citită aproape tot timpul, când ROLLBACK nu trebuie să scoată rândul și accepți lacătul de tabelă. MEMORY pentru o căutare care poate dispărea la oprire. Pentru rânduri care trebuie să rămână după commit, rămâi pe InnoDB.
ANTI-PATTERNS:
    CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MyISAM, apoi ROLLBACK după un INSERT care trebuia să dispară. Transactions este NO, deci rândul rămâne.
STAFF QUESTIONS:
    DatabaseTerminology_Staff_Interview.showEngines
    DatabaseTerminology_Staff_Interview.createInnodbTable
    DatabaseTerminology_Staff_Interview.createTableWithoutEngine
    DatabaseTerminology_Staff_Interview.createMyisamTable
    DatabaseTerminology_Staff_Interview.alterTableToInnodb
    DatabaseTerminology_Staff_Interview.createMissingEngine
    DatabaseTerminology_Staff_Interview.setDefaultStorageEngine
NOVEL TRANSFER:
    Două recepții de spital scriu același pat. Înainte să te bazezi pe ROLLBACK, citești Transactions pe motorul tabelei de paturi.
SPACED REVIEW:
    O zi: din SHOW ENGINES spui care rând are Support DEFAULT și ce înseamnă Transactions pentru un rând după ROLLBACK.
    Șapte zile: un CREATE TABLE fără ENGINE pe 9.7, și ce componentă rămâne dacă serverul a pornit cu alt default-storage-engine.
    Treizeci de zile: ENGINE=MISSING, spui dacă tabela există fără NO_ENGINE_SUBSTITUTION și cu modul pornit.
*/

/*
Definiția unui motor de stocare (storage engine):
       Componenta care execută propoziția pentru un tip de tabelă. Pe 9.7 InnoDB este implicit.

Exemplu:
       DatabaseTerminology_Terms.explainStorageEngine()
       SHOW ENGINES arată Support DEFAULT și Transactions YES pe rândul InnoDB.

Definiția unei tabele (table):
       Obiectul a cărui clauză ENGINE alege componenta care îi ține rândurile.

Exemplu:
       DatabaseTerminology_Terms.explainTable()
       CREATE TABLE cu ENGINE=INNODB leagă order_line de InnoDB la sfârșitul propoziției.

Definiția unei tranzacții (transaction):
       Unitatea pe care motorul cu Transactions YES o poate duce la commit sau o poate da înapoi.

Exemplu:
       DatabaseTerminology_Terms.explainTransaction()
       Coloana Transactions de pe SHOW ENGINES spune dacă ROLLBACK mai are ce scoate.

Definiția unui lacăt (lock):
       Granularitatea cu care motorul oprește o a doua sesiune: rând la InnoDB, tabelă la MyISAM.

Exemplu:
       DatabaseTerminology_Terms.explainLock()
       ALTER TABLE ... ENGINE=InnoDB mută tabela pe lacătul de rând.

Definiția unui index grupat (clustered index):
       La InnoDB, indexul cheii primare este locul în care stă rândul.

Exemplu:
       DatabaseTerminology_Terms.explainClusteredIndex()
       PRIMARY KEY (id) ENGINE=INNODB pune rândul în indexul lui id.
*/

/*
🟢 Începător
    Ce este            lecția care arată componenta ce va executa schimbarea pe tabelă
    De ce e nevoie     fără SHOW ENGINES, desenul bazei pare deja o tranzacție
🟡 Producție
    Cum se scrie       SHOW ENGINES, citind Support și Transactions
🔴 Staff
    Unde               înainte de INSERT. Vecinul MyISAM are Transactions NO
Pași                   1. Afișezi propoziția. 2. O trimiți la server când există un cont. 3. Alegi copilul după coloana Transactions.
*/
// Începător: clasa păstrează propoziția minimă prin care vezi motorul, înainte să scrii un rând.
// Producție: chooseEngine întoarce SHOW ENGINES. Dacă dispare clasa, rămâne comentariul de curs, fără o propoziție de trimis.
// Expert: clasa nu predă ACID, lacătul sau OLTP pe rând. Alege componenta. MyISAM nu ține tranzacția.
public final class DatabaseTerminology_Fast {

    /*
    🟢 Începător
        Ce este            pornirea care tipărește propoziția, fără să schimbe o tabelă
        De ce e nevoie     fără main, exemplul minim nu are un loc din care îl rulezi pe JVM
    🟡 Producție
        Cum se scrie       java ... DatabaseTerminology_Fast
    🔴 Staff
        Unde               JVM-ul tipărește textul. Serverul îl execută doar când un cont răspunde. Lipsa parolei din repo lasă rularea pe server nefacută
    Pași                   1. Apelează chooseEngine. 2. Tipărește textul. 3. Nu deschide JDBC.
    */
    // Începător: pornește exemplul și arată propoziția pe ecran. Niciun rând nu este scris.
    // Producție: main apelează chooseEngine și tipărește SHOW ENGINES. Dacă linia de pornire dispare, nu mai vezi propoziția dintr-o rulare locală.
    // Expert: main nu deschide o sesiune MySQL și nu ține lacăt. Rularea pe server rămâne în afara acestei linii cât timp repo-ul nu are cont.
    public static void main(String[] args) {
        // Începător: pe ecran apare propoziția cu care întrebi serverul ce motoare are.
        // Producție: System.out.println primește rezultatul lui chooseEngine, adică SHOW ENGINES. Fără apel, main nu arată nimic.
        // Expert: tipărirea nu este SHOW ENGINES executat de server. \G este decorator de client, nu face parte din propoziție.
        System.out.println(chooseEngine());
        // Începător: exemplul s-a oprit după ce a arătat propoziția. Niciun rând nu a fost scris.
        // Producție: acolada închide main. Dacă lipsește, clasa nu compilează și SHOW ENGINES nu mai pornește din această clasă.
        // Expert: închiderea nu face COMMIT și nu eliberează un lacăt, pentru că main nu a deschis o sesiune pe server.
    }

    /*
    🟢 Începător
        Ce este            propoziția care listează motoarele și spune care este cel implicit
        De ce e nevoie     fără ea alegi componenta dintr-un cuvânt scris lângă diagramă
    🟡 Producție
        Cum se scrie       SHOW ENGINES
    🔴 Staff
        Unde               Support și Transactions, înainte de scriere. InnoDB pe 9.7 este DEFAULT și Transactions YES. MyISAM este Transactions NO
    Pași                   1. Construiești textul. 2. Îl trimiți când serverul acceptă sesiunea. 3. Citești rândul motorului tabelei tale.
    */
    // Începător: metoda întoarce întrebarea pe care i-o pui serverului ca să vezi motoarele.
    // Producție: corpul întoarce SHOW ENGINES. Dacă metoda dispare, lecția nu mai are propoziția gata de trimis.
    // Expert: metoda nu creează tabela și nu deschide tranzacția. Vecinul se citește pe același rezultat: MyISAM, Transactions NO.
    public static String chooseEngine() {
        // Începător: serverul îți arată ce componentă poate ține rândurile și care este cea implicită. Rândurile tale nu se schimbă.
        // Producție: SHOW ENGINES. Coloana Support DEFAULT este motorul unui CREATE TABLE fără ENGINE, cât timp nimeni nu a schimbat default-storage-engine. Coloana Transactions NO înseamnă că ROLLBACK nu scoate rândul.
        // Expert: pe 9.7 InnoDB are Support DEFAULT, Transactions YES, lacăt de rând și FOREIGN KEY. MyISAM are Transactions NO și lacăt de tabelă. Linia nu este SET default_storage_engine și nu ține loc de COMMIT.
        return "SHOW ENGINES";
        // Începător: metoda s-a încheiat și a lăsat propoziția înapoi celui care a chemat-o.
        // Producție: acolada închide chooseEngine. Fără ea, SHOW ENGINES nu mai este o metodă compilabilă.
        // Expert: închiderea nu citește coloana Transactions. Citirea se face pe rezultatul serverului, după ce propoziția a plecat.
    }
    // Începător: lecția scurtă se oprește aici. Alegerea dintre copii a avut o singură propoziție.
    // Producție: acolada închide clasa. Fără ea, fișierul nu compilează pe Java 27.
    // Expert: clasa nu închide folderul Transaction și nu predă lacătul de rând pe o propoziție separată. Următorul pas este SHOW ENGINES pe server, când există cont.
}
