package _2_SQL_Statements._1_DDL._4_RENAME;

// https://dev.mysql.com/doc/refman/8.0/en/rename-table.html


/*                                     Rename object


    ○ RENAME object_type object_name TO new_object_name

                * RENAME TABLE tbl_name TO new_tbl_name

    ○ RENAME TABLE renames one or more tables.

                   * You must have ALTER and DROP privileges for the original table,

                   * and CREATE and INSERT privileges for the new table.


 */


/*
TOPIC:
    RENAME TABLE sales TO sales_archive mută numele obiectului; rândurile rămân în obiectul cu numele nou.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să schimbi numele fără să copiezi rândurile într-o tabelă nouă. După propoziție, sales nu mai există și sales_archive are rândurile.
MECHANISM:
    Serverul execută RENAME TABLE la sfârșitul propoziției. Cere ALTER și DROP pe numele vechi și CREATE și INSERT pe numele nou, cum spune comentariul de curs.
LOOKALIKES:
    ALTER TABLE ... RENAME COLUMN schimbă o coloană. CREATE TABLE sales_archive urmat de INSERT copiază și lasă sales pe loc.
BOOK THEORY:
    Pagina RENAME TABLE nu a fost adusă separat: NERULAT AICI. Comentariul de curs, păstrat, spune privilegiile ALTER, DROP, CREATE și INSERT. PDF local de rename nu a apărut la deschiderea PDF-urilor.
    HISTORICAL: pagina 15.1.43 a fost adusă după aceea. RENAME TABLE mută numele, cere ALTER și DROP pe tabela veche și CREATE și INSERT pe cea nouă, eșuează fără să lase o redenumire pe jumătate, nu mută privilegiile pe numele nou și nu redenumește o tabelă TEMPORARY.
BOOK EXAMPLE:
    renameSalesKeepsRows
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_4_RENAME/Rename.java
BREAK LAB:
    copyLeavesBothNames ține INSERT INTO sales_archive SELECT * FROM sales. INSERT nu scoate numele sales. Rămân două tabele.
TRADE-OFFS:
    INSERT SELECT când trebuie să păstrezi sales și să umpli o arhivă separată.
ANTI-PATTERNS:
    INSERT INTO sales_archive SELECT * FROM sales ca redenumire. sales rămâne.
STAFF QUESTIONS:
    renameSalesKeepsRows și copyLeavesBothNames. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Tabela de ture shift trebuie să devină shift_2026 fără să dublezi turele. RENAME TABLE shift TO shift_2026 lasă un singur obiect.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă RENAME TABLE sales TO sales_archive?
   Capcană: RENAME copiază rândurile și lasă sales gol.
   Cheie: renameSalesKeepsRows: rândurile stau în sales_archive și numele sales dispare. fără RENAME, o copie prin INSERT lasă și sales, și sales_archive
   Demonstrație: renameSalesKeepsRows
2. Enunț: Ce rămâne în tabelă sau în sesiune după RENAME TABLE sales TO sales_archive?
   Capcană: Până la COMMIT, ambele nume există.
   Cheie: Efectul este: rândurile stau în sales_archive și numele sales dispare. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: renameSalesKeepsRows
3. Enunț: De ce RENAME TABLE sales TO sales_archive nu este vecinul său?
   Capcană: INSERT SELECT este redenumirea, pentru că destinația are aceleași rânduri.
   Cheie: ALTER TABLE ... RENAME COLUMN schimbă o coloană. CREATE TABLE sales_archive urmat de INSERT copiază și lasă sales pe loc.
   Demonstrație: renameSalesKeepsRows
4. Enunț: Cum eșuează INSERT INTO sales_archive SELECT * FROM sales?
   Capcană: Dacă sales_archive există, RENAME le unește.
   Cheie: copyLeavesBothNames: INSERT nu scoate numele sales. Rămân două tabele.
   Demonstrație: copyLeavesBothNames
5. Enunț: Când nu mai trimiți RENAME TABLE sales TO sales_archive?
   Capcană: Ca să redenumești, faci mereu copie, ca să ai și varianta veche.
   Cheie: INSERT SELECT când trebuie să păstrezi sales și să umpli o arhivă separată.
   Demonstrație: renameSalesKeepsRows

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei un singur obiect, cu numele sales_archive -> renameSalesKeepsRows -> RENAME TABLE sales TO sales_archive -> nu garantează că un view sau un privilegiu pe numele sales s-a mutat
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> copyLeavesBothNames -> INSERT INTO sales_archive SELECT * FROM sales -> INSERT nu scoate numele sales. Rămân două tabele.
    3. Granița
           -> RENAME TABLE este propoziția. Redenumirea din panoul Workbench nu mută obiectul până nu o trimite.
    4. Alternativa
           -> ALTER TABLE ... RENAME COLUMN schimbă o coloană. CREATE TABLE sales_archive urmat de INSERT copiază și lasă sales pe loc.
    5. Anti-patternul
           -> INSERT INTO sales_archive SELECT * FROM sales ca redenumire. sales rămâne.
    6. Dovada
           -> renameSalesKeepsRows ține propoziția. copyLeavesBothNames ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> RENAME TABLE mută numele și păstrează rândurile o singură dată. INSERT SELECT copiază și lasă sursa. Dacă destinația există, RENAME eșuează.
*/

public class Rename {
    /*
    🟢 Începător
        Ce este            rândurile stau în sales_archive și numele sales dispare
        De ce e nevoie     fără RENAME, o copie prin INSERT lasă și sales, și sales_archive
    🟡 Producție
        Cum se scrie       RENAME TABLE sales TO sales_archive
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este INSERT INTO sales_archive SELECT * FROM sales, care copiază și nu scoate sursa
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT INTO sales_archive SELECT * FROM sales, care copiază și nu scoate sursa.
    */
    // Începător: renameSalesKeepsRows păstrează propoziția pentru rândurile stau în sales_archive și numele sales dispare
    // Producție: public static String renameSalesKeepsRows(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. RENAME nu filtrează rânduri și eșuează dacă sales_archive există deja.
    public static String renameSalesKeepsRows() {
        // Începător: rândurile stau în sales_archive și numele sales dispare
        // Producție: RENAME TABLE sales TO sales_archive. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: RENAME nu filtrează rânduri și eșuează dacă sales_archive există deja. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "RENAME TABLE sales TO sales_archive";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide renameSalesKeepsRows. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă sales_archive există, rândurile se copiază și sales rămâne
        De ce e nevoie     fără textul greșit, pare că o copie redenumește
    🟡 Producție
        Cum se scrie       INSERT INTO sales_archive SELECT * FROM sales
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă sales_archive există, rândurile se copiază și sales rămâne
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT INTO sales_archive SELECT * FROM sales, care copiază și nu scoate sursa.
    */
    // Începător: copyLeavesBothNames păstrează textul greșit pentru dacă sales_archive există, rândurile se copiază și sales rămâne
    // Producție: public static String copyLeavesBothNames(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. INSERT nu scoate numele sales. Rămân două tabele.
    public static String copyLeavesBothNames() {
        // Începător: dacă sales_archive există, rândurile se copiază și sales rămâne
        // Producție: INSERT INTO sales_archive SELECT * FROM sales. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: INSERT nu scoate numele sales. Rămân două tabele. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO sales_archive SELECT * FROM sales";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide copyLeavesBothNames. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
