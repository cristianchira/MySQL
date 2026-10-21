package _4_Database_Terminology.Data_Warehouse;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_data_warehouse


/*                                                   data warehouse


    ○ A database system or application that primarily runs large queries.

                  * The read-only

                  * or read-mostly data

          might be organized in denormalized form for query efficiency.

    ○ Can benefit from the optimizations for read-only transactions in MySQL 5.6 and higher.

                  * Contrast with OLTP.




 */

/*
TOPIC:
    Depozitul de date ține citiri mari pe istoric; un GROUP BY pe sales_history nu este tranzacția scurtă de OLTP.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pui o scanare de istoric în aceeași propoziție cu o vânzare curentă care trebuie să se închidă repede.
MECHANISM:
    Serverul execută agregarea pe tabela de istoric. Rândurile curente din oltp nu se mută doar pentru că ai numit depozitul.
LOOKALIKES:
    OLTP este tranzacția scurtă pe rândul curent, de exemplu UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1.
BOOK THEORY:
    Comentariul de curs, păstrat, spune citiri mari, date denormalizate, contrast cu OLTP. PDF local în folder nu a fost listat separat. Pagina SELECT 9.7 a fost deschisă pentru GROUP BY.
BOOK EXAMPLE:
    scanHistoryByDepartment
REPO DEMO:
    src/main/java/_4_Database_Terminology/Data_Warehouse/Data_Warehouse_.java
BREAK LAB:
    oltpUpdateIsNotTheWarehouse ține UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1. UPDATE este tranzacția scurtă. Scanarea de istoric este agregarea pe sales_history.
TRADE-OFFS:
    UPDATE pe rândul curent când problema este OLTP, nu raportul.
ANTI-PATTERNS:
    UPDATE unei vânzări ca să obții COUNT pe istoric. Nu apare agregatul.
STAFF QUESTIONS:
    scanHistoryByDepartment și oltpUpdateIsNotTheWarehouse. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numărul de cutii livrate anul trecut stă pe ship_history, grupat. Corectarea unei cutii de azi este UPDATE pe tabela curentă.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no?
   Capcană: UPDATE pe sales este interogarea de depozit.
   Cheie: scanHistoryByDepartment: rezultatul numără istoric pe departament și nu închide o vânzare curentă. fără să desparți încărcarea, un UPDATE de vânzare pare aceeași muncă cu scanarea
   Demonstrație: scanHistoryByDepartment
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no?
   Capcană: GROUP BY mută rândurile în depozit.
   Cheie: Efectul este: rezultatul numără istoric pe departament și nu închide o vânzare curentă. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: scanHistoryByDepartment
3. Enunț: De ce SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no nu este vecinul său?
   Capcană: OLTP și depozitul folosesc aceeași propoziție.
   Cheie: OLTP este tranzacția scurtă pe rândul curent, de exemplu UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1.
   Demonstrație: scanHistoryByDepartment
4. Enunț: Cum eșuează UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: COUNT pe sales_history închide vânzarea 1.
   Cheie: oltpUpdateIsNotTheWarehouse: UPDATE este tranzacția scurtă. Scanarea de istoric este agregarea pe sales_history.
   Demonstrație: oltpUpdateIsNotTheWarehouse
5. Enunț: Când nu mai trimiți SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no?
   Capcană: Ca să raportezi, faci UPDATE.
   Cheie: UPDATE pe rândul curent când problema este OLTP, nu raportul.
   Demonstrație: scanHistoryByDepartment

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE citești volume mari de istoric, nu corectezi vânzarea 1 -> scanHistoryByDepartment -> SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no -> nu face tabela denormalizată și nu oprește scrierile OLTP
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> oltpUpdateIsNotTheWarehouse -> UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 -> UPDATE este tranzacția scurtă. Scanarea de istoric este agregarea pe sales_history.
    3. Granița
           -> Depozitul este încărcarea. Cuvântul din diagramă nu este propoziția.
    4. Alternativa
           -> OLTP este tranzacția scurtă pe rândul curent, de exemplu UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1.
    5. Anti-patternul
           -> UPDATE unei vânzări ca să obții COUNT pe istoric. Nu apare agregatul.
    6. Dovada
           -> scanHistoryByDepartment ține propoziția. oltpUpdateIsNotTheWarehouse ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Depozitul citește mult istoric. OLTP schimbă puțin, pe rândul curent. GROUP BY pe istoric nu este UPDATE-ul unei vânzări.
*/

public class Data_Warehouse_ {
    /*
    🟢 Începător
        Ce este            rezultatul numără istoric pe departament și nu închide o vânzare curentă
        De ce e nevoie     fără să desparți încărcarea, un UPDATE de vânzare pare aceeași muncă cu scanarea
    🟡 Producție
        Cum se scrie       SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1.
    */
    // Începător: scanHistoryByDepartment păstrează propoziția pentru rezultatul numără istoric pe departament și nu închide o vânzare curentă
    // Producție: public static String scanHistoryByDepartment(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. GROUP BY pe istoric nu ține locul unei tranzacții scurte și nu normalizează tabela.
    public static String scanHistoryByDepartment() {
        // Începător: rezultatul numără istoric pe departament și nu închide o vânzare curentă
        // Producție: SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: GROUP BY pe istoric nu ține locul unei tranzacții scurte și nu normalizează tabela. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT dept_no, COUNT(*) FROM sales_history GROUP BY dept_no";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide scanHistoryByDepartment. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            se schimbă un rând curent, nu apare numărul pe istoric
        De ce e nevoie     fără textul greșit, orice SELECT pare depozit
    🟡 Producție
        Cum se scrie       UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care se schimbă un rând curent, nu apare numărul pe istoric
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1.
    */
    // Începător: oltpUpdateIsNotTheWarehouse păstrează textul greșit pentru se schimbă un rând curent, nu apare numărul pe istoric
    // Producție: public static String oltpUpdateIsNotTheWarehouse(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. UPDATE este tranzacția scurtă. Scanarea de istoric este agregarea pe sales_history.
    public static String oltpUpdateIsNotTheWarehouse() {
        // Începător: se schimbă un rând curent, nu apare numărul pe istoric
        // Producție: UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: UPDATE este tranzacția scurtă. Scanarea de istoric este agregarea pe sales_history. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide oltpUpdateIsNotTheWarehouse. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
