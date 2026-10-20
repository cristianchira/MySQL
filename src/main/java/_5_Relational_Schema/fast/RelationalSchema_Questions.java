/*
TOPIC:
    Schema relațională este dicționarul de tabele, coloane, indexuri și chei străine. FOREIGN KEY pe sales.customer_id refuză vânzarea al cărei customer nu există. CREATE SCHEMA creează doar baza goală.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    O vânzare nu are voie să rămână cu un customer_id care lipsește din customer. După CONSTRAINT sales_ibfk_1, rândul cu părintele lipsă nu rămâne. KEY customer_id îl lasă.
MECHANISM:
    Serverul verifică FOREIGN KEY la INSERT sau UPDATE în sales și la DELETE sau UPDATE în customer. Pe InnoDB refuzul este la sfârșitul propoziției: clauza omisă este NO ACTION, echivalentă cu RESTRICT. INDEX și UNIQUE nu întreabă dacă părintele există.
LOOKALIKES:
    KEY customer_id și FOREIGN KEY (customer_id) stau pe aceeași coloană din sales. Indexul poate apărea în EXPLAIN. Doar FOREIGN KEY refuză orfanul. CREATE SCHEMA creează baza. PRIMARY KEY (purchase_number) identifică vânzarea. UNIQUE (email_address) refuză emailul nenul repetat și acceptă mai multe NULL.
BOOK THEORY:
    Manualul 9.7: conceptual, schema este mulțimea de tabele, coloane, indexuri și chei străine; fizic, CREATE SCHEMA este sinonim cu CREATE DATABASE. FOREIGN KEY respinge copilul fără părinte, iar InnoDB dă propoziția înapoi. UNIQUE respinge valoarea nenulă duplicat și permite mai multe NULL. Indexul obișnuit nu respinge duplicatul. Foreign_Key.pdf, Unique_Key_And_Null_Values.pdf și Relationships.pdf descriu planul: relația, unicitatea și cardinalitatea, fără propoziția executabilă.
BOOK EXAMPLE:
    RelationalSchema_Fast.childMustHaveParent
REPO DEMO:
    src/main/java/_5_Relational_Schema/fast/RelationalSchema_Questions.java
BREAK LAB:
    SET foreign_key_checks = 0, apoi INSERT INTO sales (customer_id) VALUES (999). sales_ibfk_1 rămâne în dicționar, iar rândul orfan rămâne.
TRADE-OFFS:
    Orfan permis: KEY customer_id. Un customer o singură dată în sales: UNIQUE peste FOREIGN KEY. Mai mulți clienți pe o vânzare: tabelă de legătură.
ANTI-PATTERNS:
    KEY customer_id fără FOREIGN KEY. INSERT cu customer_id inexistent reușește.
STAFF QUESTIONS:
    RelationalSchema_Staff_Interview, întrebările 1–50
NOVEL TRANSFER:
    Bilet și călător: FOREIGN KEY (passenger_id) REFERENCES passenger (passenger_id) refuză biletul orfan.
SPACED REVIEW:
    o zi: obiectul care refuză customer_id 999
    șapte zile: KEY customer_id față de sales_ibfk_1
    treizeci de zile: domeniu nou, FOREIGN KEY, UNIQUE sau INDEX

Definiția unei scheme relaționale (relational schema):
       Conceptual, schema este mulțimea de tabele, coloane, indexuri și chei străine. În MySQL, CREATE SCHEMA este sinonim cu CREATE DATABASE și lasă baza goală.

Exemplu:
       RelationalSchema_Terms.explainRelationalSchema()
       Legătura dintre sales și customer este sales_ibfk_1, nu numele bazei.

Definiția unei chei străine (foreign key):
       Cheia străină stă pe tabela copil și respinge valoarea care nu există la părinte.

Exemplu:
       RelationalSchema_Terms.explainForeignKey()
       O vânzare cu customer_id 999 nu rămâne dacă 999 lipsește din customer.

Definiția unei tabele părinte (parent table):
       Tabela părinte ține valoarea pe care copilul o caută. Ștergerea ei depinde de ON DELETE.

Exemplu:
       RelationalSchema_Terms.explainParentTable()
       DELETE pe customer cu vânzări încă legate este respins dacă ON DELETE lipsește.

Definiția unei tabele copil (child table):
       Tabela copil este sales, cea care poartă FOREIGN KEY (customer_id).

Exemplu:
       RelationalSchema_Terms.explainChildTable()
       INSERT-ul în sales este propoziția la care se vede orfanul respins.

Definiția unui index unic (unique index):
       UNIQUE respinge valoarea nenulă duplicat și permite mai multe NULL.

Exemplu:
       RelationalSchema_Terms.explainUniqueIndex()
       Al doilea users.email_address identic nu rămâne. Două emailuri NULL rămân.

Definiția unui index secundar (secondary index):
       Indexul obișnuit, KEY sau CREATE INDEX, nu respinge duplicatul și nu caută un părinte.

Exemplu:
       RelationalSchema_Terms.explainSecondaryIndex()
       CREATE INDEX email_index lasă două emailuri identice în users.

Definiția unei relații (relational):
       Unu-la-mulți este FOREIGN KEY fără UNIQUE pe sales.customer_id. Unu-la-unu adaugă UNIQUE. Serverul nu are o propoziție RELATIONSHIP.

Exemplu:
       RelationalSchema_Terms.explainRelationship()
       Același customer_id din customer poate apărea de mai multe ori în sales.

1. Ce rămâne în sales dacă customer_id 999 nu este în customer și lipsește sales_ibfk_1?
2. Ce face ON DELETE când clauza lipsește din FOREIGN KEY, pe InnoDB?
3. De ce KEY customer_id din același CREATE TABLE nu verifică customer?
4. Ce rămâne în users după două INSERT-uri cu email_address NULL sub UNIQUE KEY unique_email_address?
5. Când adaugi UNIQUE (customer_id) peste FOREIGN KEY, și când lași repetiția în sales?
6. Ce arată SHOW CREATE TABLE după SET foreign_key_checks = 0 și un INSERT orfan?

Întrebări adăugate:
*/

// Începător: pachetul ține întrebările schemei, fără să scrie în customer sau în sales.
// Producție: package _5_Relational_Schema.fast. Fără el, RelationalSchema_Questions nu stă lângă lecție.
// Expert: pachetul nu oprește foreign_key_checks și nu creează sales_ibfk_1.
package _5_Relational_Schema.fast;

/*
🟢 Începător
    Ce este            întrebările despre alegerea dintre FOREIGN KEY, UNIQUE și indexul obișnuit pe schema din sales și users
    De ce e nevoie     fără ele, KEY customer_id pare aceeași regulă cu sales_ibfk_1
🟡 Producție
    Cum se scrie       corpul rămâne gol. Răspunsul stă în propozițiile din Relational_Schema și în RelationalSchema_Fast.childMustHaveParent
🔴 Staff
    Unde               capcana și cheia stau la întrebările din Relational_Schema.java. Aici rămân enunțurile
Pași                   1. citești enunțul
                       2. răspunsul se caută în dicționar, pe SHOW CREATE TABLE
*/
// Începător: clasa ține întrebările. Nu creează tabela și nu inserează vânzarea.
// Producție: public class RelationalSchema_Questions. Corpul este gol. Fără clasă, întrebările nu au fișier.
// Expert: un corp cu metode ar umple FAQ-ul. Cheia rămâne în Relational_Schema, nu aici.
public class RelationalSchema_Questions {
// Începător: clasa de întrebări se închide fără să atingă rândurile din sales.
// Producție: fără această acoladă, fișierul nu compilează și întrebările despre sales_ibfk_1 nu mai stau într-o clasă goală.
// Expert: acolada nu pune foreign_key_checks = 1 și nu dovedește că EXPLAIN a văzut o cheie străină.
}
