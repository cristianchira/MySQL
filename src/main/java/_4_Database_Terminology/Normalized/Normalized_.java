package _4_Database_Terminology.Normalized;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_normalized


/*                              Normalized database


    ○ A database design strategy where

                   * data is split into multiple tables,

                   * and duplicate values condensed into single rows represented by an ID,

                   * to avoid

                                 - storing,

                                 - querying,

                                 - and updating redundant or lengthy values.

                         It is typically used in OLTP applications.

    ○ For example, an address might be given a unique ID,

                  * so that a census database could represent the relationship lives at this address

                  *  by associating that ID with each member of a family,

                  * rather than storing multiple copies of a complex value such as 123 Main Street, Anytown, USA.

 */

/*
TOPIC:
    Forma normală ține un fapt o dată: adresa stă în address, iar people referă address_id, în loc să repete strada pe fiecare persoană.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să schimbi strada într-un singur rând. Persoanele rămân legate prin cheie.
MECHANISM:
    Serverul ține rândul de adresă separat. UPDATE pe address schimbă strada o dată. O tabelă lată ar repeta strada pe fiecare persoană.
LOOKALIKES:
    O coloană street pe people repetă valoarea. JOIN aduce strada, nu o copiază în fiecare UPDATE.
BOOK THEORY:
    Comentariul de curs descrie adresa cu ID. PDF local Normalized nu a fost în extras. Pagina FOREIGN KEY 9.7 a fost deschisă pentru legătura copil-părinte.
BOOK EXAMPLE:
    addressLivesOnce
REPO DEMO:
    src/main/java/_4_Database_Terminology/Normalized/Normalized_.java
BREAK LAB:
    wideTableRepeatsTheStreet ține UPDATE people SET street = '123 Main Street'. Tabela lată repetă valoarea. Forma normală ține strada în address o dată.
TRADE-OFFS:
    Tabela lată când citirea trebuie să evite îmbinarea și accepți să repeți strada.
ANTI-PATTERNS:
    UPDATE pe people fără WHERE ca să schimbi o adresă. Repeți strada pe toți.
STAFF QUESTIONS:
    addressLivesOnce și wideTableRepeatsTheStreet. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Codul de depozit stă în depot. Cutia ține depot_id. UPDATE depot schimbă numele o dată, nu pe fiecare cutie.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă UPDATE address SET street = '123 Main Street' WHERE address_id = 4?
   Capcană: UPDATE people SET street schimbă adresa o singură dată.
   Cheie: addressLivesOnce: strada se schimbă într-un rând, iar people care referă 4 văd aceeași adresă. fără tabelă separată, schimbi strada pe fiecare persoană
   Demonstrație: addressLivesOnce
2. Enunț: Ce rămâne în tabelă sau în sesiune după UPDATE address SET street = '123 Main Street' WHERE address_id = 4?
   Capcană: Normalizarea este un index.
   Cheie: Efectul este: strada se schimbă într-un rând, iar people care referă 4 văd aceeași adresă. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: addressLivesOnce
3. Enunț: De ce UPDATE address SET street = '123 Main Street' WHERE address_id = 4 nu este vecinul său?
   Capcană: JOIN copiază strada în people.
   Cheie: O coloană street pe people repetă valoarea. JOIN aduce strada, nu o copiază în fiecare UPDATE.
   Demonstrație: addressLivesOnce
4. Enunț: Cum eșuează UPDATE people SET street = '123 Main Street'?
   Capcană: address_id fără constrângere verifică părintele.
   Cheie: wideTableRepeatsTheStreet: Tabela lată repetă valoarea. Forma normală ține strada în address o dată.
   Demonstrație: wideTableRepeatsTheStreet
5. Enunț: Când nu mai trimiți UPDATE address SET street = '123 Main Street' WHERE address_id = 4?
   Capcană: Tabela lată este forma normală pentru că are mai multe coloane.
   Cheie: Tabela lată când citirea trebuie să evite îmbinarea și accepți să repeți strada.
   Demonstrație: addressLivesOnce

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE faptul stă o dată și persoanele îl referă -> addressLivesOnce -> UPDATE address SET street = '123 Main Street' WHERE address_id = 4 -> nu înlocuiește FOREIGN KEY dacă nu ai scris constrângerea
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> wideTableRepeatsTheStreet -> UPDATE people SET street = '123 Main Street' -> Tabela lată repetă valoarea. Forma normală ține strada în address o dată.
    3. Granița
           -> Forma normală este schema. Un JOIN de raport nu copiază strada înapoi în people.
    4. Alternativa
           -> O coloană street pe people repetă valoarea. JOIN aduce strada, nu o copiază în fiecare UPDATE.
    5. Anti-patternul
           -> UPDATE pe people fără WHERE ca să schimbi o adresă. Repeți strada pe toți.
    6. Dovada
           -> addressLivesOnce ține propoziția. wideTableRepeatsTheStreet ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Forma normală ține faptul o dată. Tabela lată îl repetă. UPDATE pe address schimbă un rând. Fără FOREIGN KEY, referința nu este verificată.
*/

public class Normalized_ {
    /*
    🟢 Începător
        Ce este            strada se schimbă într-un rând, iar people care referă 4 văd aceeași adresă
        De ce e nevoie     fără tabelă separată, schimbi strada pe fiecare persoană
    🟡 Producție
        Cum se scrie       UPDATE address SET street = '123 Main Street' WHERE address_id = 4
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este UPDATE people SET street = '123 Main Street', care repetă faptul pe fiecare rând atins
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE people SET street = '123 Main Street', care repetă faptul pe fiecare rând atins.
    */
    // Începător: addressLivesOnce păstrează propoziția pentru strada se schimbă într-un rând, iar people care referă 4 văd aceeași adresă
    // Producție: public static String addressLivesOnce(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Normalizarea nu este un index și nu face JOIN-ul inutil când vrei strada lângă nume.
    public static String addressLivesOnce() {
        // Începător: strada se schimbă într-un rând, iar people care referă 4 văd aceeași adresă
        // Producție: UPDATE address SET street = '123 Main Street' WHERE address_id = 4. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Normalizarea nu este un index și nu face JOIN-ul inutil când vrei strada lângă nume. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "UPDATE address SET street = '123 Main Street' WHERE address_id = 4";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide addressLivesOnce. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            fără WHERE, toate persoanele primesc aceeași stradă stocată de mai multe ori
        De ce e nevoie     fără textul greșit, tabela lată pare forma normală
    🟡 Producție
        Cum se scrie       UPDATE people SET street = '123 Main Street'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care fără WHERE, toate persoanele primesc aceeași stradă stocată de mai multe ori
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu UPDATE people SET street = '123 Main Street', care repetă faptul pe fiecare rând atins.
    */
    // Începător: wideTableRepeatsTheStreet păstrează textul greșit pentru fără WHERE, toate persoanele primesc aceeași stradă stocată de mai multe ori
    // Producție: public static String wideTableRepeatsTheStreet(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Tabela lată repetă valoarea. Forma normală ține strada în address o dată.
    public static String wideTableRepeatsTheStreet() {
        // Începător: fără WHERE, toate persoanele primesc aceeași stradă stocată de mai multe ori
        // Producție: UPDATE people SET street = '123 Main Street'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Tabela lată repetă valoarea. Forma normală ține strada în address o dată. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "UPDATE people SET street = '123 Main Street'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide wideTableRepeatsTheStreet. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
