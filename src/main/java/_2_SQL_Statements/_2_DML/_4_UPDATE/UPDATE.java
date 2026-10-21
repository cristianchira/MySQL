package _2_SQL_Statements._2_DML._4_UPDATE;

//UPDATE
// https://dev.mysql.com/doc/refman/8.0/en/update.html


/*                                              UPDATE Statement

     ○ UPDATE sales
        SET date_of_purchase = '2019-12-12'
        WHERE purchase_number=1;

    ○ UPDATE is a DML statement

                   * that modifies rows in a table.

                   * we do not have to update each value of the record of interest

                   * we can still say we have updated the specific record

                   * if you don’t provide a WHERE condition,

                                 - ALL rows of the table will be UPDATED


    ○ An UPDATE statement can start with a

                   * WITH clause to define common table expressions accessible within the UPDATE.

                   * https://dev.mysql.com/doc/refman/8.0/en/with.html


                                                 Complete UPDATE STATEMENT


UPDATE [LOW_PRIORITY] [IGNORE] table_reference
    SET assignment_list
    [WHERE where_condition]
    [ORDER BY ...]
    [LIMIT row_count]

value:
    {expr | DEFAULT}

assignment:
    col_name = value

assignment_list:
    assignment [, assignment] ...


                                                            SQL





 */


/*
TOPIC:
    UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 schimbă rândul care există; fără WHERE schimbă toate rândurile.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să corectezi data unei cumpărări, nu să adaugi una. Rândul 1 rămâne, cu data nouă.
MECHANISM:
    Serverul execută UPDATE pe rândurile care satisfac WHERE. Fără WHERE le actualizează pe toate. Nu adaugă un rând lipsă.
LOOKALIKES:
    INSERT INTO sales adaugă un rând. DELETE îl scoate. SET date_of_purchase = date_of_purchase nu scrie din nou dacă valoarea este aceeași.
BOOK THEORY:
    Manualul 9.7, 15.2.17 UPDATE, deschis în tur: UPDATE schimbă rânduri care există. Fără WHERE le schimbă pe toate. LIMIT este rânduri găsite, nu neapărat schimbate. 
BOOK EXAMPLE:
    updateOnePurchaseDate
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_4_UPDATE/UPDATE.java
BREAK LAB:
    insertDoesNotChangeTheOldRow ține INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12'). INSERT adaugă. UPDATE schimbă rândul care există. Fără WHERE, UPDATE schimbă toată tabela.
TRADE-OFFS:
    INSERT când rândul nu există. DELETE când trebuie să dispară, nu să își schimbe data.
ANTI-PATTERNS:
    UPDATE fără WHERE ca să corectezi o cumpărare. Se schimbă toate datele.
STAFF QUESTIONS:
    updateOnePurchaseDate și insertDoesNotChangeTheOldRow. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Corectezi data unui lot: UPDATE lot SET packed = '2026-10-01' WHERE lot_no = 'L9'. Fără WHERE, toate loturile primesc aceeași dată.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: UPDATE creează purchase_number 1 dacă nu era.
   Cheie: updateOnePurchaseDate: rândul 1 rămâne și are data 2019-12-12. fără WHERE, toate cumpărăturile primesc aceeași dată
   Demonstrație: updateOnePurchaseDate
2. Enunț: Ce rămâne în tabelă sau în sesiune după UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: Fără WHERE, MySQL refuză UPDATE, deci este sigur.
   Cheie: Efectul este: rândul 1 rămâne și are data 2019-12-12. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: updateOnePurchaseDate
3. Enunț: De ce UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 nu este vecinul său?
   Capcană: INSERT este forma de a schimba data.
   Cheie: INSERT INTO sales adaugă un rând. DELETE îl scoate. SET date_of_purchase = date_of_purchase nu scrie din nou dacă valoarea este aceeași.
   Demonstrație: updateOnePurchaseDate
4. Enunț: Cum eșuează INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12')?
   Capcană: SET la aceeași valoare numără mereu un rând schimbat.
   Cheie: insertDoesNotChangeTheOldRow: INSERT adaugă. UPDATE schimbă rândul care există. Fără WHERE, UPDATE schimbă toată tabela.
   Demonstrație: insertDoesNotChangeTheOldRow
5. Enunț: Când nu mai trimiți UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1?
   Capcană: Ca să corectezi o dată, ștergi rândul și îl inserezi.
   Cheie: INSERT când rândul nu există. DELETE când trebuie să dispară, nu să își schimbe data.
   Demonstrație: updateOnePurchaseDate

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE rândul există și vrei altă dată -> updateOnePurchaseDate -> UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1 -> nu adaugă rândul lipsă și fără WHERE nu se oprește la purchase_number 1
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> insertDoesNotChangeTheOldRow -> INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12') -> INSERT adaugă. UPDATE schimbă rândul care există. Fără WHERE, UPDATE schimbă toată tabela.
    3. Granița
           -> UPDATE este DML. Editarea unei celule în grid nu este propoziția până nu o trimite.
    4. Alternativa
           -> INSERT INTO sales adaugă un rând. DELETE îl scoate. SET date_of_purchase = date_of_purchase nu scrie din nou dacă valoarea este aceeași.
    5. Anti-patternul
           -> UPDATE fără WHERE ca să corectezi o cumpărare. Se schimbă toate datele.
    6. Dovada
           -> updateOnePurchaseDate ține propoziția. insertDoesNotChangeTheOldRow ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> UPDATE schimbă rânduri existente. Fără WHERE le schimbă pe toate. INSERT nu corectează rândul vechi. Rândul lipsă rămâne lipsă.
*/

public class UPDATE {
    /*
    🟢 Începător
        Ce este            rândul 1 rămâne și are data 2019-12-12
        De ce e nevoie     fără WHERE, toate cumpărăturile primesc aceeași dată
    🟡 Producție
        Cum se scrie       UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12'), care adaugă un rând și eșuează dacă 1 există deja ca cheie
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12'), care adaugă un rând și eșuează dacă 1 există deja ca cheie.
    */
    // Începător: updateOnePurchaseDate păstrează propoziția pentru rândul 1 rămâne și are data 2019-12-12
    // Producție: public static String updateOnePurchaseDate(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. UPDATE nu creează purchase_number 1 dacă lipsește. Afectează zero rânduri.
    public static String updateOnePurchaseDate() {
        // Începător: rândul 1 rămâne și are data 2019-12-12
        // Producție: UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: UPDATE nu creează purchase_number 1 dacă lipsește. Afectează zero rânduri. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "UPDATE sales SET date_of_purchase = '2019-12-12' WHERE purchase_number = 1";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide updateOnePurchaseDate. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă 1 există și este cheie, inserarea eșuează și data veche rămâne; dacă nu este cheie, apare un al doilea rând
        De ce e nevoie     fără textul greșit, INSERT pare corecția
    🟡 Producție
        Cum se scrie       INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12')
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă 1 există și este cheie, inserarea eșuează și data veche rămâne; dacă nu este cheie, apare un al doilea rând
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12'), care adaugă un rând și eșuează dacă 1 există deja ca cheie.
    */
    // Începător: insertDoesNotChangeTheOldRow păstrează textul greșit pentru dacă 1 există și este cheie, inserarea eșuează și data veche rămâne; dacă nu este cheie, apare un al doilea rând
    // Producție: public static String insertDoesNotChangeTheOldRow(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. INSERT adaugă. UPDATE schimbă rândul care există. Fără WHERE, UPDATE schimbă toată tabela.
    public static String insertDoesNotChangeTheOldRow() {
        // Începător: dacă 1 există și este cheie, inserarea eșuează și data veche rămâne; dacă nu este cheie, apare un al doilea rând
        // Producție: INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12'). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: INSERT adaugă. UPDATE schimbă rândul care există. Fără WHERE, UPDATE schimbă toată tabela. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO sales (purchase_number, date_of_purchase) VALUES (1, '2019-12-12')";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide insertDoesNotChangeTheOldRow. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
