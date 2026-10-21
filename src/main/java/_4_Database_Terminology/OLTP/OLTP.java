package _4_Database_Terminology.OLTP;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_oltp


/*                                OLTP


    ○ Acronym for “Online Transaction Processing”.

                  * A database system, or a database application,
                  * that runs a workload with many transactions,
                  * with frequent writes as well as reads,
                  * typically affecting small amounts of data at a time.


    ○ For example,
                  * an airline reservation system
                  * or an application that processes bank deposits.
          The data might be organized in normalized form for a balance between
                  * DML (insert/update/delete) efficiency
                  * and query efficiency.
          Contrast with Data Warehouse.
    ○ With its row-level locking and transactional capability,
                 *InnoDB is the ideal storage engine for MySQL tables used in OLTP applications.


 */

/*
TOPIC:
    OLTP este tranzacția scurtă pe rândul curent: UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 atinge o cumpărare.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu parcurgi istoricul ca să corectezi o vânzare. Se schimbă rândul 1.
MECHANISM:
    Serverul execută UPDATE pe rândul filtrat. InnoDB ține lacătul de rând pentru această scriere. Depozitul ar agrega sales_history.
LOOKALIKES:
    SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no este citirea mare de depozit.
BOOK THEORY:
    Comentariul de curs: multe tranzacții scurte, contrast cu depozitul, InnoDB pentru lacăt de rând. Pagina UPDATE 9.7 a fost deschisă.
BOOK EXAMPLE:
    oltpUpdatesOnePurchase
REPO DEMO:
    src/main/java/_4_Database_Terminology/OLTP/OLTP.java
BREAK LAB:
    warehouseScanChangesNothing ține SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no. Scanarea nu schimbă rândul curent. UPDATE cu WHERE este tranzacția scurtă.
TRADE-OFFS:
    Agregarea pe istoric când întrebarea este depozitul, nu corecția unei vânzări.
ANTI-PATTERNS:
    GROUP BY pe istoric ca să schimbi data cumpărării 1. Data rămâne.
STAFF QUESTIONS:
    oltpUpdatesOnePurchase și warehouseScanChangesNothing. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O încasare de ghișeu este UPDATE pe biletul curent. Raportul de încasări pe an este agregarea de istoric.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: OLTP este un ENGINE.
   Cheie: oltpUpdatesOnePurchase: se schimbă data rândului 1 și restul cumpărăturilor rămân. fără WHERE, OLTP-ul devine o scriere pe toată tabela
   Demonstrație: oltpUpdatesOnePurchase
2. Enunț: Ce rămâne în tabelă sau în sesiune după UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: GROUP BY pe sales_history schimbă vânzarea 1.
   Cheie: Efectul este: se schimbă data rândului 1 și restul cumpărăturilor rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: oltpUpdatesOnePurchase
3. Enunț: De ce UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 nu este vecinul său?
   Capcană: UPDATE fără WHERE este tot o tranzacție scurtă.
   Cheie: SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no este citirea mare de depozit.
   Demonstrație: oltpUpdatesOnePurchase
4. Enunț: Cum eșuează SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no?
   Capcană: Lacătul rămâne după COMMIT.
   Cheie: warehouseScanChangesNothing: Scanarea nu schimbă rândul curent. UPDATE cu WHERE este tranzacția scurtă.
   Demonstrație: warehouseScanChangesNothing
5. Enunț: Când nu mai trimiți UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: Depozitul și OLTP se scriu cu aceeași propoziție.
   Cheie: Agregarea pe istoric când întrebarea este depozitul, nu corecția unei vânzări.
   Demonstrație: oltpUpdatesOnePurchase

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE corectezi un rând curent -> oltpUpdatesOnePurchase -> UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 -> nu ține lacătul după COMMIT și nu este un motor numit OLTP
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> warehouseScanChangesNothing -> SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no -> Scanarea nu schimbă rândul curent. UPDATE cu WHERE este tranzacția scurtă.
    3. Granița
           -> OLTP este încărcarea. ENGINE este componenta. Nu le scrii unul în locul celuilalt.
    4. Alternativa
           -> SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no este citirea mare de depozit.
    5. Anti-patternul
           -> GROUP BY pe istoric ca să schimbi data cumpărării 1. Data rămâne.
    6. Dovada
           -> oltpUpdatesOnePurchase ține propoziția. warehouseScanChangesNothing ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> OLTP schimbă puțin, pe rândul curent, cu lacăt de rând în InnoDB. Depozitul citește mult. OLTP nu este un motor. Fără WHERE, UPDATE nu mai este scurt.
*/

public class OLTP {
    /*
    🟢 Începător
        Ce este            se schimbă data rândului 1 și restul cumpărăturilor rămân
        De ce e nevoie     fără WHERE, OLTP-ul devine o scriere pe toată tabela
    🟡 Producție
        Cum se scrie       UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este agregarea pe sales_history, care nu schimbă vânzarea 1
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu agregarea pe sales_history, care nu schimbă vânzarea 1.
    */
    // Începător: oltpUpdatesOnePurchase păstrează propoziția pentru se schimbă data rândului 1 și restul cumpărăturilor rămân
    // Producție: public static String oltpUpdatesOnePurchase(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. OLTP nu este numele unui motor. Motorul se vede pe SHOW ENGINES.
    public static String oltpUpdatesOnePurchase() {
        // Începător: se schimbă data rândului 1 și restul cumpărăturilor rămân
        // Producție: UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: OLTP nu este numele unui motor. Motorul se vede pe SHOW ENGINES. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide oltpUpdatesOnePurchase. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare un număr pe istoric și data cumpărării 1 rămâne
        De ce e nevoie     fără textul greșit, orice propoziție pe sales pare OLTP
    🟡 Producție
        Cum se scrie       SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare un număr pe istoric și data cumpărării 1 rămâne
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu agregarea pe sales_history, care nu schimbă vânzarea 1.
    */
    // Începător: warehouseScanChangesNothing păstrează textul greșit pentru apare un număr pe istoric și data cumpărării 1 rămâne
    // Producție: public static String warehouseScanChangesNothing(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Scanarea nu schimbă rândul curent. UPDATE cu WHERE este tranzacția scurtă.
    public static String warehouseScanChangesNothing() {
        // Începător: apare un număr pe istoric și data cumpărării 1 rămâne
        // Producție: SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Scanarea nu schimbă rândul curent. UPDATE cu WHERE este tranzacția scurtă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide warehouseScanChangesNothing. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
