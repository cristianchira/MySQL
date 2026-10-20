// Începător: fișierul spune pe nume componentele pe care le alegi când te uiți la o tabelă.
// Producție: pachetul _4_Database_Terminology.fast ține glosarul lângă lecție. Dacă linia dispare, metodele de termen nu mai compilează aici.
// Expert: glosarul nu înlocuiește SHOW ENGINES. Primul termen este motorul. MyISAM rămâne vecinul cu Transactions NO.
package _4_Database_Terminology.fast;

/*
🟢 Începător
    Ce este            numele pe care le folosește manualul când alegi cine execută schimbarea
    De ce e nevoie     fără ele, „bază de date” rămâne un singur cuvânt pentru schemă, server și motor
🟡 Producție
    Cum se scrie       SHOW ENGINES, CREATE TABLE ... ENGINE=INNODB, ALTER TABLE ... ENGINE=InnoDB
🔴 Staff
    Unde               coloanele Support și Transactions, lacătul de rând și indexul grupat, la sfârșitul propoziției de definire
Pași                   1. Motorul. 2. Tabela. 3. Tranzacția pe care motorul o acceptă. 4. Lacătul. 5. Indexul grupat.
*/
// Începător: clasa ține definițiile și propozițiile care le arată pe server.
// Producție: fiecare explain întoarce o propoziție gata de trimis. Dacă clasa dispare, rămân comentariile vechi din copii, fără această propoziție.
// Expert: clasa nu predă pe rând ACID, OLTP sau depozitul. Le desparte de motor prin coloana Transactions și prin ENGINE.
public final class DatabaseTerminology_Terms {

    /*
    Definiția unui motor de stocare (storage engine):
           Componenta care execută propoziția pentru un tip de tabelă. Pe MySQL 9.7, InnoDB este motorul implicit.

    Exemplu:
           DatabaseTerminology_Terms.explainStorageEngine()
           SHOW ENGINES arată pe rândul InnoDB Support DEFAULT și Transactions YES.
    */
    /*
    🟢 Începător
        Ce este            componenta care va primi INSERT, UPDATE și DELETE pentru un tip de tabelă
        De ce e nevoie     fără numele ei, diagrama pare să fi ales deja cine ține rândul
    🟡 Producție
        Cum se scrie       SHOW ENGINES
    🔴 Staff
        Unde               Support și Transactions, înainte de scriere. Vecinul MyISAM are Transactions NO
    Pași                   1. Trimiți SHOW ENGINES. 2. Citești Support. 3. Citești Transactions pe motorul tabelei.
    */
    // Începător: metoda dă propoziția cu care vezi lista de motoare. Niciun rând din tabela ta nu se schimbă.
    // Producție: explainStorageEngine întoarce SHOW ENGINES. Dacă dispare, glosarul nu mai are propoziția motorului.
    // Expert: metoda nu setează default-storage-engine. MyISAM pe același rezultat nu ține tranzacția.
    public static String explainStorageEngine() {
        // Începător: serverul îți arată motoarele și care este cel implicit. Rândurile tale rămân unde sunt.
        // Producție: SHOW ENGINES. Support DEFAULT este motorul unui CREATE TABLE fără ENGINE, dacă pornirea serverului nu a ales altul. Transactions NO înseamnă că ROLLBACK nu scoate rândul.
        // Expert: pe 9.7 InnoDB este DEFAULT, Transactions YES, lacăt de rând. MyISAM este Transactions NO și lacăt de tabelă. Linia nu creează tabela.
        return "SHOW ENGINES";
        // Începător: propoziția pleacă înapoi către cine a întrebat. Lista încă nu a fost citită de pe server.
        // Producție: acolada închide explainStorageEngine. Fără ea, SHOW ENGINES nu mai este metodă.
        // Expert: închiderea nu citește coloana Transactions. Citirea este pe rezultatul serverului.
    }

    /*
    Definiția unei tabele (table):
           Obiectul a cărui clauză ENGINE alege componenta care îi ține rândurile. Definiția se vede la sfârșitul lui CREATE TABLE.

    Exemplu:
           DatabaseTerminology_Terms.explainTable()
           order_line cu ENGINE=INNODB este legată de InnoDB imediat ce propoziția s-a terminat, înainte de primul INSERT.
    */
    /*
    🟢 Începător
        Ce este            tabela care primește un motor în clipa în care o creezi
        De ce e nevoie     fără ENGINE, te bazezi pe motorul implicit al serverului, nu pe un desen
    🟡 Producție
        Cum se scrie       CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB
    🔴 Staff
        Unde               la sfârșitul lui CREATE TABLE tabela există, încă fără rânduri de comandă. Cheia primară la InnoDB este indexul grupat
    Pași                   1. Creezi order_line. 2. Verifici motorul. 3. Abia apoi inserezi.
    */
    // Începător: metoda dă propoziția care creează tabela și îi spune cine îi ține rândurile.
    // Producție: explainTable întoarce CREATE TABLE order_line ... ENGINE=INNODB. Dacă dispare, glosarul nu mai leagă tabela de motor.
    // Expert: metoda nu inserează un rând. Vecinul este același CREATE cu ENGINE=MyISAM, care nu ține tranzacția.
    public static String explainTable() {
        // Începător: apare tabela order_line, cu o cheie, pusă pe InnoDB. Încă nu are rânduri de comandă.
        // Producție: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB. Fără ENGINE, pe 9.7 rămâne motorul implicit, care este InnoDB doar dacă pornirea nu l-a schimbat.
        // Expert: la InnoDB cheia primară este indexul grupat. Aceeași propoziție cu ENGINE=MyISAM nu dă Transactions YES și nu ține FOREIGN KEY. Linia nu face COMMIT.
        return "CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB";
        // Începător: propoziția de creare este gata. Tabela există pe server abia după ce el o execută.
        // Producție: acolada închide explainTable. Fără ea, CREATE TABLE nu mai este în glosar.
        // Expert: închiderea nu înseamnă că rândul de comandă a fost scris. CREATE lasă tabela goală.
    }

    /*
    Definiția unei tranzacții (transaction):
           Unitatea de schimbări pe care un motor cu Transactions YES o poate confirma sau o poate da înapoi. SHOW ENGINES o arată pe coloană, nu prin cuvântul ACID.

    Exemplu:
           DatabaseTerminology_Terms.explainTransaction()
           Pe rândul InnoDB, Transactions YES. Pe rândul MyISAM, Transactions NO, deci ROLLBACK nu scoate rândul.
    */
    /*
    🟢 Începător
        Ce este            semnul de pe lista de motoare care spune dacă o schimbare mai poate fi dată înapoi
        De ce e nevoie     fără coloană, cuvântul tranzacție din curs pare deja adevărat pe orice tabelă
    🟡 Producție
        Cum se scrie       SHOW ENGINES și citești coloana Transactions
    🔴 Staff
        Unde               înainte de INSERT. YES pe InnoDB înseamnă că motorul acceptă commit și rollback. NO pe MyISAM înseamnă că rândul rămâne
    Pași                   1. Trimiți SHOW ENGINES. 2. Citești Transactions pe motorul tabelei. 3. Abia apoi te bazezi pe ROLLBACK.
    */
    // Începător: metoda întoarce lista din care vezi dacă motorul tău ține o schimbare care încă nu e confirmată.
    // Producție: explainTransaction întoarce SHOW ENGINES. Dacă dispare, nu mai ai unde citi coloana Transactions.
    // Expert: metoda nu este START TRANSACTION și nu este COMMIT. ACID este proprietatea. Coloana este dovada de pe motor.
    public static String explainTransaction() {
        // Începător: te uiți la motoare ca să vezi care poate da înapoi o schimbare. Rândurile nu se scriu acum.
        // Producție: SHOW ENGINES. Citești Transactions. YES înseamnă că motorul poate confirma sau da înapoi o schimbare încă deschisă. NO înseamnă că ROLLBACK nu scoate rândul.
        // Expert: InnoDB pe 9.7 are Transactions YES. MyISAM are Transactions NO. Cuvântul OLTP nu schimbă coloana. Linia nu deschide ea însăși unitatea de lucru.
        return "SHOW ENGINES";
        // Începător: ai propoziția. Răspunsul cu YES sau NO vine de la server.
        // Producție: acolada închide explainTransaction. Fără ea, coloana Transactions nu mai are metodă în glosar.
        // Expert: închiderea nu face ROLLBACK. Efectul asupra rândului se vede când motorul cu Transactions NO a primit deja INSERT.
    }

    /*
    Definiția unui lacăt (lock):
           Granularitatea cu care motorul oprește o a doua sesiune. La InnoDB este rândul. La MyISAM este tabela.

    Exemplu:
           DatabaseTerminology_Terms.explainLock()
           ALTER TABLE order_line ENGINE=InnoDB mută tabela pe motorul cu lacăt de rând.
    */
    /*
    🟢 Începător
        Ce este            cât din tabelă se oprește când două sesiuni scriu în același timp
        De ce e nevoie     fără alegerea motorului, a doua sesiune poate aștepta toată tabela
    🟡 Producție
        Cum se scrie       ALTER TABLE order_line ENGINE=InnoDB
    🔴 Staff
        Unde               după ALTER, scrierile noi intră pe lacăt de rând. MyISAM ține lacăt de tabelă. Conversia copiază rândurile în timpul propoziției
    Pași                   1. Schimbi motorul. 2. Confirmi ENGINE. 3. Abia apoi lași două sesiuni să scrie.
    */
    // Începător: metoda dă propoziția care mută tabela pe motorul ce oprește un rând, nu toată tabela.
    // Producție: explainLock întoarce ALTER TABLE order_line ENGINE=InnoDB. Dacă dispare, glosarul nu mai arată schimbarea de lacăt.
    // Expert: ALTER nu este un LOCK TABLES. La MyISAM granularitatea rămâne tabela. Propoziția poate copia rândurile; nu se termină când ai schimbat un cuvânt într-un comentariu.
    public static String explainLock() {
        // Începător: tabela order_line trece pe InnoDB, deci o a doua sesiune nu mai așteaptă toată tabela pentru un singur rând.
        // Producție: ALTER TABLE order_line ENGINE=InnoDB. Fără propoziție, tabela rămâne pe motorul ei vechi și lacătul nu se schimbă.
        // Expert: tabelul de motoare din manualul 9.7 dă InnoDB pe rând și MyISAM pe tabelă. Linia nu eliberează un lacăt deja ținut de sesiunea cealaltă înainte de ALTER.
        return "ALTER TABLE order_line ENGINE=InnoDB";
        // Începător: propoziția de mutare este gata. Lacătul nou se vede după ce serverul termină ALTER.
        // Producție: acolada închide explainLock. Fără ea, ALTER TABLE nu mai este în glosar.
        // Expert: închiderea nu înseamnă că două sesiuni scriu deja în paralel. Asta se vede la următorul UPDATE, pe motorul nou.
    }

    /*
    Definiția unui index grupat (clustered index):
           La InnoDB, indexul cheii primare este locul în care stă rândul. MyISAM nu ține rândul în același fel.

    Exemplu:
           DatabaseTerminology_Terms.explainClusteredIndex()
           PRIMARY KEY (id) împreună cu ENGINE=INNODB pune rândul din order_line în indexul lui id.
    */
    /*
    🟢 Începător
        Ce este            locul în care InnoDB pune rândul, adică indexul cheii primare
        De ce e nevoie     fără cheie primară numită, tot există un index grupat, dar nu l-ai ales tu în propoziție
    🟡 Producție
        Cum se scrie       CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB
    🔴 Staff
        Unde               rândul stă în indexul grupat după ce INSERT trece. ENGINE=MyISAM nu face din cheia primară indexul în care stă rândul
    Pași                   1. Declari cheia primară pe InnoDB. 2. Inserezi. 3. Căutarea după id citește rândul din acel index.
    */
    // Începător: metoda dă propoziția care spune și cheia, și motorul în care rândul va sta lângă cheie.
    // Producție: explainClusteredIndex întoarce CREATE TABLE cu PRIMARY KEY și ENGINE=INNODB. Dacă dispare, glosarul nu mai arată unde stă rândul.
    // Expert: indexul grupat este al InnoDB, nu al oricărui PRIMARY KEY. Pe MyISAM cheia primară nu este indexul grupat din acest manual.
    public static String explainClusteredIndex() {
        // Începător: creezi order_line cu o cheie primară pe InnoDB, ca rândul să stea în indexul acelei chei.
        // Producție: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB. Fără PRIMARY KEY, InnoDB tot are un index grupat, dar cheia nu este id-ul pe care l-ai numit tu.
        // Expert: manualul 9.7 spune că fiecare tabelă InnoDB are indexul grupat pe cheia primară. MyISAM are B-tree, nu index grupat. Linia nu inserează rândul; locul se umple la INSERT.
        return "CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB";
        // Începător: propoziția este gata. Rândul apare în index abia la un INSERT de mai târziu.
        // Producție: acolada închide explainClusteredIndex. Fără ea, cheia primară nu mai are metodă în glosar.
        // Expert: închiderea nu garantează că un SELECT după id evită citirea întregii tabele. Planul se vede cu EXPLAIN, pe o propoziție de citire.
    }
    // Începător: glosarul se oprește la motor, tabelă, tranzacție, lacăt și indexul grupat.
    // Producție: acolada închide clasa. Fără ea, fișierul nu compilează.
    // Expert: termenii de OLTP, depozit și ACID rămân în copiii lor. Aici i-ai despărțit de ENGINE.
}
