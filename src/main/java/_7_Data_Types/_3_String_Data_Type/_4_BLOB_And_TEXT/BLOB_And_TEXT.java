package _7_Data_Types._3_String_Data_Type._4_BLOB_And_TEXT;

// The BLOB and TEXT Types
// https://dev.mysql.com/doc/refman/8.0/en/blob.html

// Martin G. DATE, DATETIME, TIMESTAMP BLOB  5: 08
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339036#questions


/*                    The BLOB and TEXT Types

    ○ refers to a file of binary data

                * involves saving files .jpg,   . doc


    ○ CREATE TABLE mytable (
       id INT NOT NULL AUTO_INCREMENT,
       blob_column BLOB,
       PRIMARY KEY (id)
       );

       INSERT INTO mytable (blob_column) VALUES ('binary data');


                         The four BLOB types are


    1. TINYBLOB,

    2. BLOB,

    3. MEDIUMBLOB,

    4.LONGBLOB


    ○ Insert a photo from your computer into a table in MySQL

                * using the LOAD_FILE() function

                * along with an INSERT statement.

    ○  sql script

                * -- Create table
                  CREATE TABLE photos (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255),
                      image BLOB
                    );

                    -- Set file path and load file
                    SET @photo = LOAD_FILE('/path/to/photo.jpg');

                    -- Insert photo into table
                    INSERT INTO photos (name, image) VALUES ('My Photo', @photo);

*/


/*
TOPIC:
    BLOB ține octeți; TEXT ține caractere. Un fișier de imagine nu este un VARCHAR scurt.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să stea imaginea în BLOB, nu într-un șir de caractere. Rândul de metadate poate sta lângă ea, dar tipul nu se amestecă.
MECHANISM:
    Serverul stochează BLOB ca octeți și TEXT ca caractere. LOAD_FILE citește un fișier de pe hostul serverului, nu de pe discul clientului.
LOOKALIKES:
    VARCHAR(255) nu ține o imagine. BINARY este scurt și fix, nu un BLOB de fișier.
BOOK THEORY:
    PDF-ul din acest folder, deschis în tur, începe cu DATE, nu cu BLOB: nu conține pe primele pagini efectul BLOB. Pagina BLOB ca URL separat este NERULAT AICI. Comentariul de curs spune octeți și LOAD_FILE.
BOOK EXAMPLE:
    blobHoldsTheImage
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/_4_BLOB_And_TEXT/BLOB_And_TEXT.java
BREAK LAB:
    textIsNotThePhoto ține CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image TEXT). TEXT este caracter. BLOB este octet. LOAD_FILE nu citește discul clientului.
TRADE-OFFS:
    TEXT când valoarea este un text lung, nu o imagine.
ANTI-PATTERNS:
    TEXT pentru poză. Octeții nu sunt caractere.
STAFF QUESTIONS:
    blobHoldsTheImage și textIsNotThePhoto. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Radiografia este BLOB. Raportul scris este TEXT. Calea de pe laptop nu este LOAD_FILE.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB)?
   Capcană: TEXT ține octeții pozei.
   Cheie: blobHoldsTheImage: image poate ține octeții pozei, name ține caractere, iar tabela încă nu are poza. fără BLOB, VARCHAR nu este locul pentru octeții unui fișier
   Demonstrație: blobHoldsTheImage
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB)?
   Capcană: CREATE TABLE photos încarcă fișierul.
   Cheie: Efectul este: image poate ține octeții pozei, name ține caractere, iar tabela încă nu are poza. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: blobHoldsTheImage
3. Enunț: De ce CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB) nu este vecinul său?
   Capcană: LOAD_FILE citește discul clientului.
   Cheie: VARCHAR(255) nu ține o imagine. BINARY este scurt și fix, nu un BLOB de fișier.
   Demonstrație: blobHoldsTheImage
4. Enunț: Cum eșuează CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image TEXT)?
   Capcană: BLOB și VARCHAR(255) țin la fel o imagine.
   Cheie: textIsNotThePhoto: TEXT este caracter. BLOB este octet. LOAD_FILE nu citește discul clientului.
   Demonstrație: textIsNotThePhoto
5. Enunț: Când nu mai trimiți CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB)?
   Capcană: BINARY(4) este locul unei poze.
   Cheie: TEXT când valoarea este un text lung, nu o imagine.
   Demonstrație: blobHoldsTheImage

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE păstrezi octeții unui fișier -> blobHoldsTheImage -> CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB) -> nu încarcă poza doar prin CREATE TABLE și nu citește o cale de pe client
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> textIsNotThePhoto -> CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image TEXT) -> TEXT este caracter. BLOB este octet. LOAD_FILE nu citește discul clientului.
    3. Granița
           -> BLOB este tipul. Alegerea unui fișier în Workbench nu este LOAD_FILE până nu ajunge pe server.
    4. Alternativa
           -> VARCHAR(255) nu ține o imagine. BINARY este scurt și fix, nu un BLOB de fișier.
    5. Anti-patternul
           -> TEXT pentru poză. Octeții nu sunt caractere.
    6. Dovada
           -> blobHoldsTheImage ține propoziția. textIsNotThePhoto ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> BLOB este octet. TEXT este caracter. CREATE TABLE nu încarcă fișierul. LOAD_FILE citește hostul serverului.
*/

public class BLOB_And_TEXT {
    /*
    🟢 Începător
        Ce este            image poate ține octeții pozei, name ține caractere, iar tabela încă nu are poza
        De ce e nevoie     fără BLOB, VARCHAR nu este locul pentru octeții unui fișier
    🟡 Producție
        Cum se scrie       CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este image TEXT, care ține caractere, nu octeții unei poze
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu image TEXT, care ține caractere, nu octeții unei poze.
    */
    // Începător: blobHoldsTheImage păstrează propoziția pentru image poate ține octeții pozei, name ține caractere, iar tabela încă nu are poza
    // Producție: public static String blobHoldsTheImage(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. LOAD_FILE('/path/to/photo.jpg') citește de pe server. Calea de pe calculatorul clientului nu este fișierul serverului.
    public static String blobHoldsTheImage() {
        // Începător: image poate ține octeții pozei, name ține caractere, iar tabela încă nu are poza
        // Producție: CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: LOAD_FILE('/path/to/photo.jpg') citește de pe server. Calea de pe calculatorul clientului nu este fișierul serverului. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image BLOB)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide blobHoldsTheImage. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            image este caracter, nu octetul unei poze
        De ce e nevoie     fără textul greșit, TEXT pare BLOB lizibil
    🟡 Producție
        Cum se scrie       CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image TEXT)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care image este caracter, nu octetul unei poze
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu image TEXT, care ține caractere, nu octeții unei poze.
    */
    // Începător: textIsNotThePhoto păstrează textul greșit pentru image este caracter, nu octetul unei poze
    // Producție: public static String textIsNotThePhoto(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TEXT este caracter. BLOB este octet. LOAD_FILE nu citește discul clientului.
    public static String textIsNotThePhoto() {
        // Începător: image este caracter, nu octetul unei poze
        // Producție: CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image TEXT). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: TEXT este caracter. BLOB este octet. LOAD_FILE nu citește discul clientului. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE photos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), image TEXT)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide textIsNotThePhoto. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
