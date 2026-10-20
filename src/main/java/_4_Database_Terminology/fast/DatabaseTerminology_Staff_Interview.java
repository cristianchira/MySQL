// Începător: fișierul ține întrebările de interviu despre cine execută schimbarea pe tabelă.
// Producție: pachetul _4_Database_Terminology.fast. Metodele întorc propoziția, nu un text despre propoziție.
// Expert: banca este despre motorul de pe SHOW ENGINES. Nu se lipește pe WHERE: aici se alege componenta, nu filtrul.
package _4_Database_Terminology.fast;

/*
TOPIC:
    Motorul de stocare numit de ENGINE, sau InnoDB când clauza lipsește pe MySQL 9.7, este componenta care execută schimbarea de rânduri pe acea tabelă și care decide dacă ROLLBACK mai poate scoate rândul.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Problema este să numești schema sau încărcarea și să crezi că rândul stă deja într-o tranzacție.
MECHANISM:
    Serverul încarcă motorul din ENGINE sau pe cel implicit. SHOW ENGINES arată Support și Transactions înainte de scriere.
LOOKALIKES:
    MyISAM. Îi separă Transactions NO și lacătul de tabelă.
BOOK THEORY:
    Pe MySQL 9.7 InnoDB este implicit, cu commit, rollback, recuperare după oprire, lacăt de rând, index grupat și FOREIGN KEY. MyISAM are Transactions NO și lacăt de tabelă. ENGINE=MISSING fără NO_ENGINE_SUBSTITUTION avertizează și creează tabela cu motorul implicit; cu modul pornit, tabela nu se creează. PDF-ul SQL+Keywords.pdf nu descrie motorul.
BOOK EXAMPLE:
    DatabaseTerminology_Fast.chooseEngine
REPO DEMO:
    src/main/java/_4_Database_Terminology/fast/DatabaseTerminology_Staff_Interview.java
BREAK LAB:
    CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING
TRADE-OFFS:
    MyISAM când accepți Transactions NO. MEMORY când căutarea poate dispărea la oprire. InnoDB când commit trebuie să păstreze rândul.
ANTI-PATTERNS:
    ENGINE=MyISAM, apoi ROLLBACK după un INSERT care trebuia să dispară.
STAFF QUESTIONS:
    DatabaseTerminology_Staff_Interview.showEngines
    DatabaseTerminology_Staff_Interview.createInnodbTable
    DatabaseTerminology_Staff_Interview.createTableWithoutEngine
    DatabaseTerminology_Staff_Interview.createMyisamTable
    DatabaseTerminology_Staff_Interview.alterTableToInnodb
    DatabaseTerminology_Staff_Interview.createMissingEngine
    DatabaseTerminology_Staff_Interview.setDefaultStorageEngine
NOVEL TRANSFER:
    Patul de spital: citești Transactions înainte să anulezi a doua ocupare.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile.
*/

/*
Definiția unui motor de stocare (storage engine):
       Componenta care execută propoziția pentru un tip de tabelă.

Exemplu:
       DatabaseTerminology_Terms.explainStorageEngine()
       SHOW ENGINES, InnoDB, Support DEFAULT, Transactions YES.
*/

/*
🟢 Începător
    Ce este            banca care te pune să recunoști motorul din propoziție, din rezultat sau din eroare
    De ce e nevoie     fără ea, răspunsul rămâne „baza este relațională”, fără efect pe rând
🟡 Producție
    Cum se scrie       șapte metode cu propoziția și cincizeci de întrebări în comentariu
🔴 Staff
    Unde               Support, Transactions, lacătul și momentul de la sfârșitul lui CREATE sau ALTER
Pași                   1. Recunoști. 2. Spui ce se întâmplă cu rândul. 3. Îl desparți de MyISAM. 4. Arăți eșecul. 5. Alegi.
*/
// Începător: clasa ține propozițiile pe care le trimiți ca să alegi motorul.
// Producție: nu are main. Răspunsul este scris lângă întrebare. Dacă dispare o metodă citată, întrebarea rămâne fără propoziție.
// Expert: cele cincizeci stau pe acest motor. Nu întreabă de filtrul din WHERE și nu predau fiecare copil până la capăt.
public final class DatabaseTerminology_Staff_Interview {

    /*
    🟢 Începător
        Ce este            lista de motoare și semnul celui implicit
        De ce e nevoie     fără ea nu știi dacă ROLLBACK mai poate scoate un rând
    🟡 Producție
        Cum se scrie       SHOW ENGINES
    🔴 Staff
        Unde               Support și Transactions, înainte de INSERT
    Pași                   1. Trimiți propoziția. 2. Citești InnoDB. 3. Îl compari cu MyISAM.
    */
    // Începător: metoda dă întrebarea prin care vezi motoarele. Nu scrie rânduri.
    // Producție: showEngines întoarce SHOW ENGINES. Dacă dispare, întrebările de recunoaștere nu mai au propoziție.
    // Expert: metoda nu deschide tranzacția. MyISAM pe același rezultat are Transactions NO.
    public static String showEngines() {
        // Începător: serverul arată ce componentă poate ține rândurile și care este implicită.
        // Producție: SHOW ENGINES. Support DEFAULT este motorul unui CREATE fără ENGINE, dacă pornirea nu a schimbat default-storage-engine. Transactions NO înseamnă că ROLLBACK nu scoate rândul.
        // Expert: pe 9.7 InnoDB are Support DEFAULT, Transactions YES și lacăt de rând. MyISAM are Transactions NO și lacăt de tabelă. \G este decorator de client, nu face parte din propoziție.
        return "SHOW ENGINES";
        // Începător: propoziția este gata de trimis. Rezultatul vine de la server.
        // Producție: acolada închide showEngines. Fără ea, SHOW ENGINES nu mai compilează în bancă.
        // Expert: închiderea nu citește coloana Transactions. Citirea este pe rezultat.
    }

    /*
    🟢 Începător
        Ce este            crearea tabelei de linii de comandă pe motorul care ține tranzacția
        De ce e nevoie     fără ENGINE te bazezi pe implicitul serverului
    🟡 Producție
        Cum se scrie       CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB
    🔴 Staff
        Unde               tabela există la sfârșitul propoziției, goală. Cheia primară este indexul grupat
    Pași                   1. Creezi. 2. Confirmi motorul. 3. Abia apoi inserezi.
    */
    // Începător: metoda dă propoziția care leagă order_line de InnoDB.
    // Producție: createInnodbTable întoarce CREATE TABLE ... ENGINE=INNODB. Dacă dispare, întrebarea despre rând și index nu mai are propoziție.
    // Expert: metoda nu inserează. Vecinul cu același corp și ENGINE=MyISAM nu ține FOREIGN KEY.
    public static String createInnodbTable() {
        // Începător: apare tabela order_line pe InnoDB, cu o cheie, încă fără rânduri de comandă.
        // Producție: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB. Fără clauza ENGINE, pe 9.7 se folosește motorul implicit.
        // Expert: la InnoDB rândul viitor stă în indexul cheii primare, lacătul de scriere este pe rând, iar o schimbare încă deschisă poate fi dată înapoi. Linia nu face COMMIT.
        return "CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB";
        // Începător: propoziția de creare s-a încheiat în metodă. Pe server, tabela apare la sfârșitul execuției.
        // Producție: acolada închide createInnodbTable. Fără ea, ENGINE=INNODB nu mai este în bancă.
        // Expert: închiderea nu umple indexul grupat. Îl umple INSERT-ul următor.
    }

    /*
    🟢 Începător
        Ce este            crearea aceleiași tabele fără să scrii motorul
        De ce e nevoie     fără să știi implicitul, omiți ENGINE și primești motorul cu care a pornit serverul
    🟡 Producție
        Cum se scrie       CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id))
    🔴 Staff
        Unde               pe 9.7, implicitul din manual este InnoDB, până când default-storage-engine sau pornirea spun altfel
    Pași                   1. Creezi fără ENGINE. 2. Verifici cu SHOW ENGINES sau cu definiția tabelei. 3. Nu presupui după anul cursului.
    */
    // Începător: metoda dă un CREATE care lasă serverul să aleagă motorul.
    // Producție: createTableWithoutEngine întoarce CREATE TABLE order_line fără ENGINE. Dacă dispare, nu mai ai propoziția care depinde de implicit.
    // Expert: pe 9.7 manualul spune că lipsa clauzei creează InnoDB. Un --default-storage-engine la pornire schimbă asta. Metoda nu citește variabila.
    public static String createTableWithoutEngine() {
        // Începător: creezi order_line și lași serverul să pună motorul lui implicit. Tabela încă nu are rânduri de comandă.
        // Producție: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)). Pe 9.7, fără altă setare, componenta este InnoDB. Dacă linia primește un ENGINE, nu mai testează implicitul.
        // Expert: manualul 9.7 spune că CREATE TABLE fără ENGINE creează InnoDB. SET default_storage_engine din sesiune, sau opțiunea de la pornire, schimbă componenta. Linia nu arată singură ce s-a ales.
        return "CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id))";
        // Începător: propoziția este gata. Motorul real se vede după ce serverul a creat tabela.
        // Producție: acolada închide createTableWithoutEngine. Fără ea, CREATE fără ENGINE nu mai este în bancă.
        // Expert: închiderea nu este dovada că motorul este InnoDB. Dovada este definiția tabelei după execuție.
    }

    /*
    🟢 Începător
        Ce este            aceeași tabelă pusă pe motorul care nu ține tranzacția
        De ce e nevoie     fără contrastul din propoziție, ambele motoare par să dea ROLLBACK înapoi
    🟡 Producție
        Cum se scrie       CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MyISAM
    🔴 Staff
        Unde               Transactions NO și lacăt de tabelă. FOREIGN KEY nu este ținut de acest motor
    Pași                   1. Creezi cu MyISAM. 2. Inserezi. 3. ROLLBACK lasă rândul.
    */
    // Începător: metoda dă propoziția care pune order_line pe MyISAM.
    // Producție: createMyisamTable întoarce CREATE TABLE ... ENGINE=MyISAM. Dacă dispare, întrebarea despre vecin nu mai are propoziție.
    // Expert: MyISAM nu este depozitul de date scris altfel. Este motorul cu Transactions NO. OLTP rămâne un cuvânt de încărcare.
    public static String createMyisamTable() {
        // Începător: tabela order_line este creată pe un motor care nu poate da înapoi un rând deja scris.
        // Producție: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MyISAM. După INSERT, ROLLBACK nu scoate rândul. Fără ENGINE=MyISAM, pe 9.7 ai cădea pe InnoDB.
        // Expert: manualul 9.7 dă MyISAM Transactions NO, lacăt de tabelă și FOREIGN KEY No. InnoDB are Transactions YES, lacăt de rând și FOREIGN KEY. Linia nu este un LOCK TABLES.
        return "CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MyISAM";
        // Începător: propoziția de creare pe MyISAM este gata. Rândul rămâne abia după un INSERT urmat de ROLLBACK.
        // Producție: acolada închide createMyisamTable. Fără ea, ENGINE=MyISAM nu mai este în bancă.
        // Expert: închiderea nu execută ROLLBACK. Eșecul se vede la propoziția următoare, când rândul încă este în tabelă.
    }

    /*
    🟢 Începător
        Ce este            mutarea unei tabele deja create pe InnoDB
        De ce e nevoie     fără ALTER, un comentariu nou nu schimbă motorul rândurilor deja acolo
    🟡 Producție
        Cum se scrie       ALTER TABLE order_line ENGINE=InnoDB
    🔴 Staff
        Unde               la sfârșitul lui ALTER, rândurile sunt pe componenta nouă. Propoziția poate copia tabela
    Pași                   1. Alternezi motorul. 2. Confirmi definiția. 3. Scrierile de după ALTER intră pe lacăt de rând.
    */
    // Începător: metoda dă propoziția care mută order_line pe InnoDB.
    // Producție: alterTableToInnodb întoarce ALTER TABLE order_line ENGINE=InnoDB. Dacă dispare, conversia nu mai are propoziție.
    // Expert: ALTER ENGINE nu este un redenumit de schemă. Copiază rândurile în timpul propoziției. Lacătul de rând începe pentru scrierile de după.
    public static String alterTableToInnodb() {
        // Începător: order_line trece pe InnoDB, iar rândurile ei ajung la componenta care ține tranzacția.
        // Producție: ALTER TABLE order_line ENGINE=InnoDB. Fără propoziție, motorul vechi rămâne și ROLLBACK se poartă ca înainte.
        // Expert: manualul 9.7 spune că ALTER TABLE ... ENGINE schimbă motorul. Dacă InnoDB nu este disponibil și NO_ENGINE_SUBSTITUTION este pornit, tabela nu este alterată. Linia nu eliberează un lacăt luat înainte de ea.
        return "ALTER TABLE order_line ENGINE=InnoDB";
        // Începător: propoziția de mutare este gata. Efectul se vede când serverul termină ALTER.
        // Producție: acolada închide alterTableToInnodb. Fără ea, ALTER ENGINE nu mai este în bancă.
        // Expert: închiderea nu înseamnă că sesiunile care scriau pe motorul vechi au trecut singure pe lacăt de rând.
    }

    /*
    🟢 Începător
        Ce este            un CREATE care cere un motor ce nu există
        De ce e nevoie     fără el, crezi că numele scris rămâne componenta care ține rândul
    🟡 Producție
        Cum se scrie       CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING
    🔴 Staff
        Unde               fără NO_ENGINE_SUBSTITUTION, avertisment și tabela pe motorul implicit. Cu modul pornit, eroare și tabela nu există
    Pași                   1. Trimiți ENGINE=MISSING. 2. Citești avertismentul sau eroarea. 3. Verifici dacă order_line există și pe ce motor.
    */
    // Începător: metoda dă propoziția greșită, cu un motor care nu este în server.
    // Producție: createMissingEngine întoarce CREATE TABLE ... ENGINE=MISSING. Dacă dispare, eșecul cel mai mic nu mai are propoziție.
    // Expert: numele MISSING nu rămâne pe tabelă. Substituția o face serverul, sau o refuză când NO_ENGINE_SUBSTITUTION este pornit.
    public static String createMissingEngine() {
        // Începător: ceri un motor care nu există. Poți rămâne cu o tabelă pe alt motor, sau fără tabelă.
        // Producție: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING. Fără NO_ENGINE_SUBSTITUTION serverul avertizează și creează tabela cu motorul implicit. Cu modul pornit, eroarea lasă schema fără order_line.
        // Expert: manualul 9.7 spune că motorul indisponibil este înlocuit cu implicitul, cu avertisment, iar NO_ENGINE_SUBSTITUTION oprește crearea. Linia nu conține SET-ul de mod. Modul este stare de sesiune, nu parte din numele tabelei.
        return "CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING";
        // Începător: propoziția greșită este gata. Simptomul se vede în avertisment sau în eroare, și în faptul că order_line există sau nu.
        // Producție: acolada închide createMissingEngine. Fără ea, ENGINE=MISSING nu mai este în bancă.
        // Expert: închiderea nu pornește NO_ENGINE_SUBSTITUTION. Fără modul, pericolul este tabela creată pe alt motor decât numele scris.
    }

    /*
    🟢 Începător
        Ce este            alegerea motorului pentru tabelele create de acum înainte, în această sesiune
        De ce e nevoie     fără ea, fiecare CREATE fără ENGINE ia implicitul cu care a pornit serverul
    🟡 Producție
        Cum se scrie       SET default_storage_engine=InnoDB
    🔴 Staff
        Unde               sesiunea curentă. Nu schimbă o tabelă deja creată și nu ține loc de opțiunea de la pornire
    Pași                   1. Setezi implicitul. 2. CREATE fără ENGINE intră pe InnoDB. 3. O tabelă veche rămâne pe motorul ei până la ALTER.
    */
    // Începător: metoda dă propoziția care fixează motorul implicit al sesiunii pe InnoDB.
    // Producție: setDefaultStorageEngine întoarce SET default_storage_engine=InnoDB. Dacă dispare, alegerea pentru CREATE fără ENGINE nu mai are propoziție.
    // Expert: SET de sesiune nu mută order_line dacă ea există deja. MEMORY este aceeași propoziție cu alt nume, doar când căutarea poate dispărea la oprire.
    public static String setDefaultStorageEngine() {
        // Începător: de acum, în această sesiune, o tabelă nouă fără ENGINE intră pe InnoDB. Tabelele vechi rămân cum erau.
        // Producție: SET default_storage_engine=InnoDB. Manualul 9.7 arată forma SET default_storage_engine=.... Fără propoziție, CREATE fără ENGINE rămâne pe implicitul de la pornire.
        // Expert: setarea de sesiune nu este --default-storage-engine de la pornire și nu este ALTER TABLE. Pentru o căutare care poate pieri la oprire, aceeași formă cu MEMORY este alternativa. Pentru comenzi care trebuie să rămână după commit, lași InnoDB.
        return "SET default_storage_engine=InnoDB";
        // Începător: propoziția de implicit este gata. Se vede la următorul CREATE fără ENGINE.
        // Producție: acolada închide setDefaultStorageEngine. Fără ea, SET default_storage_engine nu mai este în bancă.
        // Expert: închiderea nu schimbă motorul unei tabele deja create. Pentru aia rămâne ALTER TABLE ... ENGINE.
    }

    /*
    1. Recunoaștere.
    Enunț: SHOW ENGINES are pe InnoDB Support DEFAULT și Transactions YES. Ce componentă execută un CREATE TABLE fără ENGINE pe MySQL 9.7, dacă pornirea nu a schimbat implicitul, și ce spune coloana despre ROLLBACK?
    Capcană: „DEFAULT înseamnă că toate motoarele de pe listă țin tranzacția.”
    Cheie: Motorul de stocare. InnoDB este implicitul din manualul 9.7. Transactions YES înseamnă că o schimbare încă deschisă poate fi dată înapoi. MyISAM, pe același rezultat, are Transactions NO.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    2. Recunoaștere.
    Enunț: Pe același SHOW ENGINES, MyISAM are Transactions NO. Ce rămâne în tabelă după INSERT și ROLLBACK?
    Capcană: „ROLLBACK golește rândul pe orice motor, pentru că este un cuvânt SQL.”
    Cheie: MyISAM. Transactions NO înseamnă că ROLLBACK nu scoate rândul. InnoDB este rândul cu Transactions YES.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    3. Recunoaștere.
    Enunț: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB. Cine execută un INSERT ulterior în order_line?
    Capcană: „ENGINE este un comentariu de documentație. Serverul alege singur la fiecare INSERT.”
    Cheie: InnoDB, componenta numită la CREATE. INSERT-ul ulterior intră la ea. Tabela există de la sfârșitul lui CREATE, încă fără rândul de comandă.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    4. Recunoaștere.
    Enunț: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) fără ENGINE, server 9.7, implicit neschimbat. Ce componentă ține tabela?
    Capcană: „Fără ENGINE tabela nu are motor, deci nu poate ține tranzacție.”
    Cheie: InnoDB, pentru că manualul 9.7 spune că lipsa clauzei creează o tabelă InnoDB. Dovada după execuție este definiția tabelei, nu absența cuvântului.
    Demonstrație: DatabaseTerminology_Staff_Interview.createTableWithoutEngine()

    5. Recunoaștere.
    Enunț: ALTER TABLE order_line ENGINE=InnoDB pe o tabelă care era MyISAM. Când se schimbă componenta care ține rândurile deja existente?
    Capcană: „ALTER doar schimbă un etichetă. Rândurile rămân pe motorul vechi până la următorul INSERT.”
    Cheie: La sfârșitul lui ALTER TABLE ... ENGINE. Rândurile sunt mutate pe InnoDB de acea propoziție. Un comentariu nu face mutarea.
    Demonstrație: DatabaseTerminology_Staff_Interview.alterTableToInnodb()

    6. Recunoaștere.
    Enunț: Primești eroarea că motorul nu este disponibil și order_line nu există. Ce mod era pornit lângă ENGINE=MISSING?
    Capcană: „Orice motor necunoscut lasă tabela cu numele scris în ENGINE, ca să vedem ce am cerut.”
    Cheie: NO_ENGINE_SUBSTITUTION. Cu el, CREATE sau ALTER eșuează și tabela nu este creată sau alterată. Fără el, ai fi văzut un avertisment și tabela pe motorul implicit.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMissingEngine()

    7. Recunoaștere.
    Enunț: ENGINE=MISSING, fără NO_ENGINE_SUBSTITUTION, și un avertisment. Pe ce componentă există order_line?
    Capcană: „Avertismentul este informativ. Tabela a rămas cu motorul MISSING.”
    Cheie: Pe motorul implicit, nu pe numele din propoziție. Manualul 9.7 spune că serverul substituie implicitul și avertizează.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMissingEngine()

    8. Recunoaștere.
    Enunț: SET default_storage_engine=InnoDB, apoi un CREATE fără ENGINE în aceeași sesiune. Ce componentă primește tabela nouă?
    Capcană: „SET schimbă și tabelele create ieri.”
    Cheie: InnoDB pentru CREATE-ul de după SET, în sesiunea asta. Tabelele deja create rămân pe motorul lor până la ALTER TABLE ... ENGINE.
    Demonstrație: DatabaseTerminology_Staff_Interview.setDefaultStorageEngine()

    9. Recunoaștere.
    Enunț: Comentariul din script spune „data warehouse”, iar CREATE are ENGINE=INNODB. Ce decide cine ține rândul?
    Capcană: „Eticheta de depozit alege motorul, pentru că depozitul este o bază specială.”
    Cheie: ENGINE=INNODB. Cuvântul depozit descrie încărcarea, nu componenta. SHOW ENGINES nu are un rând OLTP și nu are un rând warehouse.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    10. Recunoaștere.
    Enunț: În tabelul de motoare, Locking granularity este Row la InnoDB și Table la MyISAM. Ce propoziție te pune pe una dintre ele?
    Capcană: „Granularitatea se setează cu un indiciu de index, nu cu motorul.”
    Cheie: ENGINE de pe CREATE sau ALTER. InnoDB ia rândul. MyISAM ia tabela. Indexul nu schimbă coloana Locking granularity.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    11. Rânduri și moment.
    Enunț: Imediat după CREATE TABLE order_line (...) ENGINE=INNODB, înainte de INSERT, ce există în schemă și ce rând de comandă există?
    Capcană: „CREATE scrie și un rând gol, ca să existe indexul.”
    Cheie: Tabela există la sfârșitul lui CREATE. Rândul de comandă încă nu. Cheia primară este definită, iar la InnoDB ea este indexul grupat în care va sta rândul.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    12. Rânduri și moment.
    Enunț: order_line este InnoDB, Transactions YES, iar o schimbare încă este în unitatea deschisă. Ce face ROLLBACK cu rândul acela?
    Capcană: „InnoDB păstrează orice INSERT, iar ROLLBACK este doar un jurnal.”
    Cheie: Motorul poate da înapoi schimbarea care încă ține de unitate, pentru că Transactions este YES. Efectul se vede la sfârșitul lui ROLLBACK, nu la comentariul ACID.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    13. Rânduri și moment.
    Enunț: order_line este MyISAM. După INSERT rulezi ROLLBACK. Ce rând rămâne?
    Capcană: „Dacă propoziția a fost într-un script cu START TRANSACTION, MyISAM intră și el în unitate.”
    Cheie: Rândul rămâne. Transactions NO. START TRANSACTION nu dă acestui motor capacitatea pe care coloana o arată NO.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    14. Lacăt.
    Enunț: Două sesiuni fac UPDATE pe același id în order_line, motor InnoDB. Ce se oprește?
    Capcană: „InnoDB oprește toată tabela, ca să fie sigur.”
    Cheie: Lacătul de rând al InnoDB. A doua sesiune așteaptă rândul, nu toate rândurile din order_line. Manualul 9.7 dă locking granularity Row.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    15. Lacăt.
    Enunț: Aceeași două sesiuni pe order_line creată cu ENGINE=MyISAM. Ce se oprește?
    Capcană: „Și MyISAM oprește doar rândul atins, pentru că WHERE are cheia primară.”
    Cheie: Lacătul de tabelă. Cheia din WHERE nu schimbă granularitatea motorului. MyISAM ține tabela.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    16. Index.
    Enunț: CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=INNODB, apoi un INSERT. Unde stă rândul?
    Capcană: „Cheia primară este doar un nume. Rândul stă într-o grămadă separată pe orice motor.”
    Cheie: În indexul grupat al InnoDB, pe cheia primară. Manualul 9.7 spune că fiecare tabelă InnoDB are acest index. MyISAM nu are index grupat în tabelul de motoare.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    17. Constrângere.
    Enunț: Vrei ca o linie fără comandă-părinte să fie refuzată. Pe ce motor are FOREIGN KEY efect, după tabelul din manualul 9.7?
    Capcană: „FOREIGN KEY este o propoziție a serverului, deci MyISAM o ține la fel.”
    Cheie: InnoDB, unde Foreign key support este Yes. La MyISAM este No. Coloana fără motorul care o ține nu verifică părintele.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    18. Durată.
    Enunț: Commit-ul pe InnoDB s-a terminat cu succes. Ce promite manualul despre oprirea bruscă de după?
    Capcană: „Commit-ul ține doar până la următorul restart, pe orice motor la fel.”
    Cheie: InnoDB are recuperare după oprire pentru datele duse la commit. Promisiunea este a motorului cu Transactions YES, nu a etichetei OLTP.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    19. Moment.
    Enunț: Rulezi doar SHOW ENGINES. Ce s-a schimbat în order_line?
    Capcană: „SHOW ENGINES deschide tranzacția și ia lacăt pe tabelele InnoDB.”
    Cheie: Nimic în rândurile din order_line. Propoziția citește lista de motoare. Schimbarea de rând începe la INSERT, UPDATE sau DELETE, pe motorul deja legat.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    20. Moment.
    Enunț: ALTER TABLE order_line ENGINE=InnoDB tocmai s-a terminat. De când un UPDATE așteaptă un rând, nu toată tabela?
    Capcană: „Lacătul de rând se aplică și scrierilor care erau deja în curs pe MyISAM, retroactiv.”
    Cheie: Pentru scrierile de după ALTER. Propoziția mută tabela. Nu rescrie un lacăt de tabelă deja luat înainte de ea.
    Demonstrație: DatabaseTerminology_Staff_Interview.alterTableToInnodb()

    21. Vecin.
    Enunț: De ce ENGINE=INNODB nu este aceeași alegere cu cuvântul „transaction” pus într-un comentariu?
    Capcană: „Dacă documentația spune tranzacție, motorul este deja tranzacțional.”
    Cheie: ENGINE leagă componenta. Cuvântul nu încarcă motorul. Transactions se citește pe SHOW ENGINES.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    22. Vecin.
    Enunț: De ce scrierea „ACID” lângă CREATE nu înlocuiește ENGINE?
    Capcană: „ACID este o clauză. O pui în loc de ENGINE.”
    Cheie: ACID este proprietatea pe care InnoDB o respectă la DML, cu commit, rollback și recuperare. Nu este un nume de motor. Componenta se alege cu ENGINE sau cu implicitul.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    23. Vecin.
    Enunț: De ce un lacăt de rând nu se cere cu o clauză separată, în locul motorului?
    Capcană: „Pun LOCK pe coloană în CREATE și motorul poate rămâne MyISAM.”
    Cheie: Granularitatea vine cu motorul. InnoDB este Row. MyISAM este Table. ENGINE este propoziția care alege.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    24. Vecin.
    Enunț: Aplicația este OLTP. De ce asta nu alege motorul?
    Capcană: „OLTP este sinonim cu InnoDB, deci CREATE poate omite orice verificare.”
    Cheie: OLTP este încărcarea: multe scrieri scurte. Motorul se vede pe ENGINE sau pe implicit. O tabelă OLTP cu ENGINE=MyISAM tot are Transactions NO.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    25. Vecin.
    Enunț: Rapoartele de istoric sunt un depozit. De ce depozitul nu este numele motorului?
    Capcană: „Depozitul înseamnă MyISAM. Îl scrii în loc de ENGINE.”
    Cheie: Depozitul este încărcarea de citiri mari. MyISAM este un motor des folosit la citire, cu Transactions NO. Poți ține istoricul și pe InnoDB. Clauza rămâne ENGINE.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    26. Vecin.
    Enunț: De ce DBMS-ul nu este motorul de stocare?
    Capcană: „MySQL, schema și InnoDB sunt același lucru, deci GRANT ține loc de ENGINE.”
    Cheie: DBMS-ul este serverul care primește propoziția. Motorul este componenta care execută DML pentru tipul de tabelă. SHOW ENGINES listează componente, nu conturi.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    27. Vecin.
    Enunț: Diagrama entitate-relație este aprobată. De ce order_line poate fi tot pe un motor cu Transactions NO?
    Capcană: „Diagrama creează tabela pe InnoDB, pentru că este o bază relațională.”
    Cheie: Diagrama nu este o propoziție. CREATE TABLE ... ENGINE este propoziția. Click-ul din Workbench nu ține locul coloanei Transactions.
    Demonstrație: DatabaseTerminology_Staff_Interview.createTableWithoutEngine()

    28. Vecin.
    Enunț: Schema este normalizată, un fapt o dată. De ce normalizarea nu alege motorul?
    Capcană: „Forma normală implică InnoDB și lacăt de rând.”
    Cheie: Normalizarea desparte tabelele ca să nu repeți valoarea. ENGINE alege componenta. O schemă normalizată pe MyISAM tot are Transactions NO.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    29. Vecin.
    Enunț: De ce autocommit nu este motorul?
    Capcană: „Dacă autocommit este 1, MyISAM începe să țină ROLLBACK ca InnoDB.”
    Cheie: Autocommit este felul în care sesiunea închide unitatea. Transactions este coloana motorului. MyISAM rămâne NO chiar dacă sesiunea închide singură fiecare propoziție. InnoDB rămâne motorul care poate da înapoi o schimbare încă deschisă.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    30. Vecin.
    Enunț: MEMORY și InnoDB pot răspunde repede. De ce nu sunt aceeași alegere pentru order_line?
    Capcană: „MEMORY ține și el commit-ul după oprirea serverului, doar că este mai rapid.”
    Cheie: MEMORY ține datele în RAM și le pierde la oprire. InnoDB are recuperare după oprire și Transactions YES. Pentru linia de comandă care trebuie să rămână, alegi InnoDB.
    Demonstrație: DatabaseTerminology_Staff_Interview.setDefaultStorageEngine()

    31. Eșec.
    Enunț: Ați creat order_line cu ENGINE=MyISAM, ați inserat o linie dublă și ați dat ROLLBACK. Ce găsește următorul SELECT?
    Capcană: „Scriptul a avut ROLLBACK, deci linia dublă nu poate fi acolo.”
    Cheie: Linia dublă este în tabelă. Transactions NO. Reparația este ENGINE=InnoDB înainte de scrierea care trebuie să poată dispărea, nu un al doilea ROLLBACK.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    32. Eșec.
    Enunț: Migrarea a rulat ENGINE=MISSING fără NO_ENGINE_SUBSTITUTION. Dimineața, order_line există. De ce nu știi din numele din script ce componentă o ține?
    Capcană: „Dacă scriptul a trecut, motorul din script este motorul tabelei.”
    Cheie: Serverul a substituit implicitul și a avertizat. Numele MISSING nu este componenta. Verifici definiția tabelei, nu ecoul scriptului.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMissingEngine()

    33. Eșec.
    Enunț: Același ENGINE=MISSING, cu NO_ENGINE_SUBSTITUTION, în deploy. Ce lipsește din schemă?
    Capcană: „Eroarea este doar un avertisment. Tabela e creată și putem insera.”
    Cheie: order_line nu este creată. Manualul spune că modul oprește CREATE sau ALTER când motorul nu este disponibil.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMissingEngine()

    34. Eșec.
    Enunț: Raportul de citire și încasarea scriu în același timp pe o tabelă MyISAM. De ce se așteaptă reciproc?
    Capcană: „Se așteaptă pentru că lipsește un index pe cheia primară.”
    Cheie: Lacătul de tabelă al MyISAM. Indexul nu schimbă granularitatea. InnoDB ar fi oprit rândul atins, nu toată tabela.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    35. Eșec.
    Enunț: Ați scris FOREIGN KEY pe o tabelă MyISAM și ați inserat un copil fără părinte. Ce s-a întâmplat cu copilul?
    Capcană: „Serverul refuză copilul pe orice motor, pentru că a văzut cuvintele FOREIGN KEY.”
    Cheie: Copilul poate rămâne. La MyISAM, Foreign key support este No în manualul 9.7. Refuzul este la InnoDB.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    36. Eșec.
    Enunț: După o oprire bruscă, vă bazați pe recuperarea InnoDB pentru o tabelă creată ENGINE=MyISAM. Ce promisiune nu aveți?
    Capcană: „Recuperarea după oprire este a serverului, deci motorul nu contează.”
    Cheie: Promisiunea de commit, rollback și recuperare din manual este la InnoDB. MyISAM are Transactions NO. Nu moștenește recuperarea InnoDB.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    37. Eșec.
    Enunț: Ați dat SET default_storage_engine=InnoDB într-o sesiune, ați închis clientul și ați deschis altul. Un CREATE fără ENGINE ia din nou alt implicit. De ce?
    Capcană: „SET schimbă serverul pentru toate sesiunile viitoare.”
    Cheie: SET default_storage_engine din exemplul manualului ține de sesiune când îl rulezi așa. Implicitul de după restart este opțiunea de pornire sau fișierul de configurare, nu ultima sesiune închisă.
    Demonstrație: DatabaseTerminology_Staff_Interview.setDefaultStorageEngine()

    38. Eșec.
    Enunț: order_line este InnoDB, iar order_export este MyISAM, în aceeași schemă. Un ROLLBACK după scrieri în ambele ce promite pentru export?
    Capcană: „Tranzacția schemei acoperă toate tabelele, orice motor ar avea.”
    Cheie: Manualul 9.7 spune că poți amesteca motoare în aceeași schemă. MyISAM are Transactions NO. ROLLBACK nu scoate rândul din export doar pentru că order_line este InnoDB.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    39. Eșec.
    Enunț: ALTER TABLE order_line ENGINE=InnoDB pe o tabelă mare durează mult. Ce face propoziția în timpul acela?
    Capcană: „Este un redenumit de metadată, deci se termină imediat și rândurile nu se mișcă.”
    Cheie: Schimbarea de motor mută rândurile pe componenta nouă. Timpul este al copierii, nu al unui comentariu. Efectul se vede la sfârșitul lui ALTER.
    Demonstrație: DatabaseTerminology_Staff_Interview.alterTableToInnodb()

    40. Eșec.
    Enunț: Sursa ține order_line pe InnoDB, iar replica a fost creată cu alt motor, pentru viteză. Ce durabilitate nu se copiază singură din numele sursei?
    Capcană: „Replica moștenește Transactions YES pentru că binlog-ul vine de la InnoDB.”
    Cheie: Motorul tabelei de pe replica este cel din CREATE-ul ei. Manualul dă chiar cazul în care sursa stă pe InnoDB pentru siguranță, iar replica folosește alt motor și pierde din durabilitate sau din concurență.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    41. Alternativă.
    Enunț: O căutare de coduri poștale poate dispărea la oprire și nu participă la ROLLBACK. Ce motor lași acolo, și ce lași pe order_line?
    Capcană: „MEMORY pe ambele, ca să fie totul rapid.”
    Cheie: MEMORY pentru căutarea care poate pieri. InnoDB pentru order_line, unde commit trebuie să păstreze rândul. Aceeași schemă poate ține ambele.
    Demonstrație: DatabaseTerminology_Staff_Interview.setDefaultStorageEngine()

    42. Alternativă.
    Enunț: Istoricul se citește rar și nu trebuie să intre în ROLLBACK-ul comenzii. Când nu pui totuși MyISAM doar pentru că ai auzit „depozit”?
    Capcană: „Depozit înseamnă obligatoriu MyISAM, iar lacătul de tabelă nu deranjează la un import zilnic care scrie.”
    Cheie: MyISAM când accepți Transactions NO și lacăt de tabelă pe durata scrierii. Dacă importul scrie în timp ce rapoartele citesc, lacătul de tabelă le oprește. InnoDB rămâne alegerea când scrierea și citirea se suprapun.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    43. Alternativă.
    Enunț: Diagrama stă în unealta de modelare. Unde trebuie să existe ENGINE ca rândul să aibă motor?
    Capcană: „Dacă diagrama e în repository, serverul a primit deja motorul.”
    Cheie: În CREATE TABLE sau ALTER TABLE trimis la server. Diagrama poate rămâne în aplicația de desen. Ea nu execută DML.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    44. Alternativă.
    Enunț: Pe 9.7 omiți ENGINE, pentru că manualul spune că implicitul este InnoDB. Când omiți totuși o verificare?
    Capcană: „Manualul garantează InnoDB și dacă my.cnf are alt default-storage-engine.”
    Cheie: Omiți ENGINE doar după ce știi implicitul acestui server. --default-storage-engine la pornire sau SET din sesiune schimbă CREATE-ul fără clauză. SHOW ENGINES arată cine este DEFAULT acum.
    Demonstrație: DatabaseTerminology_Staff_Interview.createTableWithoutEngine()

    45. Alternativă.
    Enunț: Încă desenezi entitățile și nu vrei rânduri. Ce nu trimiți la server?
    Capcană: „Trimitem CREATE pe MyISAM, gol, ca să rezervăm numele, și îl convertim cândva.”
    Cheie: Nu trimiți CREATE dacă nu vrei o tabelă. Un CREATE, chiar gol de rânduri, leagă numele de un motor în clipa aceea. Desenul rămâne în unealtă până ai ENGINE-ul pe care îl vrei.
    Demonstrație: DatabaseTerminology_Staff_Interview.createTableWithoutEngine()

    46. Alternativă.
    Enunț: Un fișier CSV trebuie să iasă spre un tabel de calcul, iar comenzile trebuie să rămână după commit. Cum împarți motoarele?
    Capcană: „Exportul CSV înlocuiește order_line, ca să avem un singur loc.”
    Cheie: InnoDB rămâne pe order_line. CSV este tabela de schimb, neindexată, folosită la import sau export. Manualul spune că în operarea normală datele stau în InnoDB.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    47. Alternativă.
    Enunț: Ai nevoie și de FOREIGN KEY, și de două sesiuni care scriu rânduri diferite. Ce motor alegi și ce cost plătești?
    Capcană: „MyISAM, ca să evităm lacătul, și FOREIGN KEY rămâne scris în CREATE.”
    Cheie: InnoDB. Primești refuzul copilului fără părinte și lacăt de rând. Costul este lacătul de rând la scriere, nu dispariția lui. MyISAM nu îți dă FOREIGN KEY din tabelul de motoare.
    Demonstrație: DatabaseTerminology_Staff_Interview.createInnodbTable()

    48. Alternativă.
    Enunț: Un rând de stoc este actualizat de multe sesiuni în același timp. Când refuzi MyISAM?
    Capcană: „MyISAM este mai simplu, iar cheia primară face lacătul să fie pe rând.”
    Cheie: Refuzi MyISAM când sesiunile trebuie să se aștepte doar pe rândul atins. MyISAM ține lacăt de tabelă. InnoDB ține rândul.
    Demonstrație: DatabaseTerminology_Staff_Interview.createMyisamTable()

    49. Alternativă.
    Enunț: Înainte de o migrare de motor, ce propoziție rulezi ca să vezi Transactions, nu după incident?
    Capcană: „Așteptăm primul ROLLBACK eșuat, ca să avem un caz real.”
    Cheie: SHOW ENGINES înainte de INSERT-ul care trebuie să poată dispărea. Coloana Transactions este semnul. Incidentul cu rândul rămas este proba de după, nu planul.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()

    50. Alternativă.
    Enunț: SHOW ENGINES a arătat Transactions YES pe motorul tabelei de paturi. Ce copil deschizi după aceea, și ce copil lași pe mai târziu?
    Capcană: „Deschidem depozitul de date, pentru că YES înseamnă raport mare.”
    Cheie: Deschizi tranzacția: commit și rollback pe motorul care a spus YES. Lacătul de rând este al InnoDB, nu un topic separat pe care îl presupunem pe MyISAM. Depozitul rămâne încărcarea de citiri, nu următorul pas al unei ocupări de pat care trebuie să poată fi dată înapoi.
    Demonstrație: DatabaseTerminology_Staff_Interview.showEngines()
    */
    // Începător: banca se oprește la alegerea motorului. Rândul de pat se anulează doar dacă Transactions este YES.
    // Producție: acolada închide clasa. Fără ea, cele șapte propoziții nu compilează.
    // Expert: nu am deschis aici COMMIT, izolarea sau forma normală până la capăt. Următorul pas, după YES, este propoziția de tranzacție din copilul Transaction.
}
