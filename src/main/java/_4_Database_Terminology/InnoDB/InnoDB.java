package _4_Database_Terminology.InnoDB;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_innodb


/*                                  InnoDB


    ○ A MySQL component that combines
                   * high performance
                   * with transactional capability
           for
                    * reliability,
                    * robustness,
                    * and concurrent access.

    ○ It embodies

                    * the ACID design philosophy.
                    * Represented as a storage engine;
                    * it handles tables created or altered with the ENGINE=INNODB clause.

    ○ În manualul MySQL 9.7, InnoDB este capitolul 17. Motoarele alternative sunt capitolul 18.
    HISTORICAL: See Chapter 15, The InnoDB Storage Engine for architectural details and administration procedures.
    Regula citită în tur: https://dev.mysql.com/doc/refman/9.7/en/innodb-introduction.html este 17.1 Introduction to InnoDB. https://dev.mysql.com/doc/refman/9.7/en/storage-engines.html este capitolul 18, Alternative Storage Engines.


    ○ and Section 8.5,
                   * “Optimizing for InnoDB Tables” for performance advice.

    ○ In MySQL 5.5 and higher,
                   * InnoDB is the default storage engine for
                                 - new tables and the ENGINE=INNODB clause is not required.

    ○ InnoDB tables are ideally suited for hot backups.

    ○ See Section 30.2,
                   * “MySQL Enterprise Backup Overview”
           for information about
                   * the MySQL Enterprise Backup product for backing up MySQL servers
                   * without interrupting normal processing.







 */

/*
TOPIC:
    ENGINE=INNODB leagă sales de motorul care ține tranzacția și lacătul de rând; MyISAM nu ține tranzacția.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să rămână posibil un ROLLBACK pe rândurile din sales. După CREATE TABLE, obiectul este InnoDB dacă motorul este disponibil.
MECHANISM:
    Serverul citește ENGINE la CREATE TABLE. SHOW ENGINES arată Transactions înainte să te bazezi pe ROLLBACK. MyISAM are Transactions NO.
LOOKALIKES:
    SHOW ENGINES nu creează tabela. ENGINE=MyISAM nu îți dă FOREIGN KEY și nici ROLLBACK pe rând.
BOOK THEORY:
    Comentariul de curs are deja HISTORICAL pe capitolul vechi. Pe 9.7, InnoDB este motorul implicit pentru o tabelă nouă dacă pornirea nu a schimbat implicitul. PDF local InnoDB nu a fost în lista scurtă; regula de Transactions este cea din lecția DatabaseTerminology_Fast, deja în repo, și din comentariul acestui fișier.
BOOK EXAMPLE:
    createSalesInnoDB
REPO DEMO:
    src/main/java/_4_Database_Terminology/InnoDB/InnoDB.java
BREAK LAB:
    myisamDropsTheTransaction ține CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM. MyISAM nu este motorul cu Transactions YES. InnoDB este vecinul care ține commit, rollback și FOREIGN KEY.
TRADE-OFFS:
    SHOW ENGINES înainte, când nu știi dacă InnoDB este disponibil. MyISAM doar când accepți că nu ai tranzacție.
ANTI-PATTERNS:
    ENGINE=MyISAM când trebuie să dai înapoi un INSERT. Rândul nu stă într-o tranzacție.
STAFF QUESTIONS:
    createSalesInnoDB și myisamDropsTheTransaction. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Tabela de probe clinice care trebuie să poată dispărea la ROLLBACK se creează ENGINE=INNODB, nu MyISAM.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE sales (purchase_number INT) ENGINE=INNODB?
   Capcană: ENGINE=MyISAM ține ROLLBACK ca InnoDB.
   Cheie: createSalesInnoDB: sales este tabelă InnoDB, cu tranzacție și lacăt de rând, și încă nu are rânduri. fără ENGINE, te bazezi pe implicitul serverului; MyISAM explicit nu ține ROLLBACK
   Demonstrație: createSalesInnoDB
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE sales (purchase_number INT) ENGINE=INNODB?
   Capcană: CREATE TABLE ENGINE=INNODB inserează un rând.
   Cheie: Efectul este: sales este tabelă InnoDB, cu tranzacție și lacăt de rând, și încă nu are rânduri. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: createSalesInnoDB
3. Enunț: De ce CREATE TABLE sales (purchase_number INT) ENGINE=INNODB nu este vecinul său?
   Capcană: SHOW ENGINES creează sales.
   Cheie: SHOW ENGINES nu creează tabela. ENGINE=MyISAM nu îți dă FOREIGN KEY și nici ROLLBACK pe rând.
   Demonstrație: createSalesInnoDB
4. Enunț: Cum eșuează CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM?
   Capcană: InnoDB este un tip de coloană.
   Cheie: myisamDropsTheTransaction: MyISAM nu este motorul cu Transactions YES. InnoDB este vecinul care ține commit, rollback și FOREIGN KEY.
   Demonstrație: myisamDropsTheTransaction
5. Enunț: Când nu mai trimiți CREATE TABLE sales (purchase_number INT) ENGINE=INNODB?
   Capcană: Fără ENGINE, pe 9.7 tabela nouă este MyISAM.
   Cheie: SHOW ENGINES înainte, când nu știi dacă InnoDB este disponibil. MyISAM doar când accepți că nu ai tranzacție.
   Demonstrație: createSalesInnoDB

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei tranzacția și lacătul de rând pe sales -> createSalesInnoDB -> CREATE TABLE sales (purchase_number INT) ENGINE=INNODB -> nu inserează rânduri și nu schimbă implicitul altor tabele
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> myisamDropsTheTransaction -> CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM -> MyISAM nu este motorul cu Transactions YES. InnoDB este vecinul care ține commit, rollback și FOREIGN KEY.
    3. Granița
           -> ENGINE este clauza. Cuvântul InnoDB într-un slide nu leagă tabela.
    4. Alternativa
           -> SHOW ENGINES nu creează tabela. ENGINE=MyISAM nu îți dă FOREIGN KEY și nici ROLLBACK pe rând.
    5. Anti-patternul
           -> ENGINE=MyISAM când trebuie să dai înapoi un INSERT. Rândul nu stă într-o tranzacție.
    6. Dovada
           -> createSalesInnoDB ține propoziția. myisamDropsTheTransaction ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> ENGINE=INNODB leagă motorul cu tranzacție și lacăt de rând. MyISAM nu ține tranzacția. SHOW ENGINES arată Transactions înainte de scriere. CREATE TABLE nu inserează rândul.
*/

public class InnoDB {
    /*
    🟢 Începător
        Ce este            sales este tabelă InnoDB, cu tranzacție și lacăt de rând, și încă nu are rânduri
        De ce e nevoie     fără ENGINE, te bazezi pe implicitul serverului; MyISAM explicit nu ține ROLLBACK
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT) ENGINE=INNODB
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM.
    */
    // Începător: createSalesInnoDB păstrează propoziția pentru sales este tabelă InnoDB, cu tranzacție și lacăt de rând, și încă nu are rânduri
    // Producție: public static String createSalesInnoDB(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ENGINE=INNODB nu inserează un rând și nu înlocuiește SHOW ENGINES când vrei să vezi Transactions.
    public static String createSalesInnoDB() {
        // Începător: sales este tabelă InnoDB, cu tranzacție și lacăt de rând, și încă nu are rânduri
        // Producție: CREATE TABLE sales (purchase_number INT) ENGINE=INNODB. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: ENGINE=INNODB nu inserează un rând și nu înlocuiește SHOW ENGINES când vrei să vezi Transactions. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT) ENGINE=INNODB";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createSalesInnoDB. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă motorul este acceptat, sales nu ține tranzacția; ROLLBACK nu scoate un INSERT comis de lipsa tranzacției
        De ce e nevoie     fără textul greșit, orice ENGINE pare InnoDB
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă motorul este acceptat, sales nu ține tranzacția; ROLLBACK nu scoate un INSERT comis de lipsa tranzacției
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM.
    */
    // Începător: myisamDropsTheTransaction păstrează textul greșit pentru dacă motorul este acceptat, sales nu ține tranzacția; ROLLBACK nu scoate un INSERT comis de lipsa tranzacției
    // Producție: public static String myisamDropsTheTransaction(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. MyISAM nu este motorul cu Transactions YES. InnoDB este vecinul care ține commit, rollback și FOREIGN KEY.
    public static String myisamDropsTheTransaction() {
        // Începător: dacă motorul este acceptat, sales nu ține tranzacția; ROLLBACK nu scoate un INSERT comis de lipsa tranzacției
        // Producție: CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: MyISAM nu este motorul cu Transactions YES. InnoDB este vecinul care ține commit, rollback și FOREIGN KEY. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide myisamDropsTheTransaction. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
