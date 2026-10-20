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
    Manualul 9.7: conceptual, schema este mulțimea de tabele, coloane, indexuri și chei străine; fizic, CREATE SCHEMA este sinonim cu CREATE DATABASE. FOREIGN KEY respinge copilul fără părinte, iar InnoDB dă propoziția înapoi. Fără ON DELETE, acțiunea este NO ACTION. CASCADE actualizează copiii în aceeași propoziție și, implicit, nu pornește trigger-e. UNIQUE permite mai multe NULL. Indexul obișnuit nu respinge duplicatul.
BOOK EXAMPLE:
    RelationalSchema_Fast.childMustHaveParent
REPO DEMO:
    src/main/java/_5_Relational_Schema/fast/RelationalSchema_Staff_Interview.java
BREAK LAB:
    SET foreign_key_checks = 0, apoi INSERT INTO sales (customer_id) VALUES (999). sales_ibfk_1 rămâne, rândul orfan rămâne.
TRADE-OFFS:
    Orfan permis: KEY customer_id. Un customer o singură dată: UNIQUE peste FOREIGN KEY. Mai mulți clienți pe o vânzare: tabelă de legătură.
ANTI-PATTERNS:
    KEY customer_id fără FOREIGN KEY. INSERT cu customer_id inexistent reușește.
STAFF QUESTIONS:
    întrebările 1–50 din acest fișier
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

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: SHOW CREATE TABLE sales conține CONSTRAINT sales_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE. Ce verifică serverul la INSERT INTO sales (customer_id) VALUES (999)?
       Capcană: „Verifică doar că există indexul KEY customer_id.”
       Cheie: FOREIGN KEY refuză 999 dacă lipsește din customer.customer_id. KEY customer_id nu face verificarea. Pe InnoDB efectul este la sfârșitul INSERT.
    2. Enunț: Propoziția este CREATE INDEX customer_id ON sales (customer_id), fără CONSTRAINT. INSERT cu 999 reușește. Ce obiect lipsește?
       Capcană: „Indexul este cheia străină, doar că nu are nume de constraint.”
       Cheie: Lipsește FOREIGN KEY. Indexul secundar permite același customer_id de mai multe ori și permite orfanul.
    3. Enunț: CREATE TABLE users (... UNIQUE KEY unique_email_address (email_address)). Al doilea INSERT cu același email eșuează, două email NULL rămân. Ce regulă este?
       Capcană: „UNIQUE este PRIMARY KEY, deci NULL este interzis.”
       Cheie: UNIQUE refuză valoarea nenulă duplicat și permite mai multe NULL. PRIMARY KEY este una și nu acceptă NULL. Aici nu există părinte.
    4. Enunț: CREATE SCHEMA shop. Există acum legătura dintre sales și customer?
       Capcană: „SCHEMA înseamnă diagrama cu foreign key.”
       Cheie: CREATE SCHEMA este sinonim cu CREATE DATABASE. Baza shop este goală. Legătura este CONSTRAINT în CREATE TABLE.
    5. Enunț: EXPLAIN pe SELECT din sales WHERE customer_id = 7 arată indexul customer_id. Ai demonstrat FOREIGN KEY?
       Capcană: „Dacă EXPLAIN folosește indexul, constrângerea există.”
       Cheie: EXPLAIN arată planul. SHOW CREATE TABLE arată sales_ibfk_1. Indexul poate exista fără FOREIGN KEY.
    6. Enunț: La INSERT în sales serverul spune că nu poate adăuga sau actualiza rândul copil. Care clauză a produs refuzul?
       Capcană: „PRIMARY KEY (purchase_number) a respins customer_id.”
       Cheie: Refuzul de părinte lipsă este FOREIGN KEY. PRIMARY KEY (purchase_number) respinge purchase_number duplicat sau NULL.
    7. Enunț: În users, UNIQUE (email_address) și două rânduri cu email_address NULL. Schema este stricată?
       Capcană: „NULL este duplicat, deci UNIQUE a eșuat.”
       Cheie: Un UNIQUE permite mai multe NULL. Ambele rânduri rămân. Nu îl transforma în PRIMARY KEY doar ca să dispară NULL.
    8. Enunț: DELETE FROM customer WHERE customer_id = 7, iar sales are ON DELETE CASCADE pe sales_ibfk_1. Ce rânduri dispar?
       Capcană: „Se șterge doar customer, vânzările rămân cu id-ul vechi.”
       Cheie: CASCADE șterge și rândurile din sales cu customer_id 7, în aceeași propoziție. Fără clauză, NO ACTION respinge ștergerea părintelui și ambele rămân.
    9. Enunț: În CREATE TABLE sales sunt și KEY customer_id, și CONSTRAINT sales_ibfk_1. Care refuză orfanul?
       Capcană: „Amândouă, pentru că stau pe aceeași coloană.”
       Cheie: Doar FOREIGN KEY. KEY este indexul cu care verificarea evită citirea întregii tabele. Poate fi creat automat dacă lipsește.
    10. Enunț: ALTER TABLE sales ADD UNIQUE KEY one_customer_once (customer_id), iar FOREIGN KEY există deja. Câte vânzări poate avea același customer nenul?
       Capcană: „UNIQUE nu schimbă relația, doar grăbește căutarea.”
       Cheie: UNIQUE face ca customer_id nenul să apară o dată: unu-la-unu. FOREIGN KEY singur lăsa unul-la-mulți. Mai multe NULL pot sta dacă coloana permite NULL.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: Pe InnoDB, când vezi că 999 nu a rămas în sales?
       Capcană: „La COMMIT, pentru că verificarea este amânată.”
       Cheie: InnoDB respinge imediat, la sfârșitul INSERT. NDB cu NO ACTION poate amâna până la commit.
    12. Enunț: INSERT-ul orfan eșuează pe InnoDB. Rămân rândurile scrise mai devreme în aceeași propoziție?
       Capcană: „Motorul se oprește la rând și lasă rândurile dinainte.”
       Cheie: Pe InnoDB, propoziția care încalcă FOREIGN KEY, UNIQUE sau PRIMARY KEY este dată înapoi. Pe un motor netranzacțional, serverul se oprește la rândul greșit și lasă rândurile deja scrise.
    13. Enunț: Cu ON DELETE CASCADE, când dispar vânzările copil?
       Capcană: „La o propoziție ulterioară de curățare.”
       Cheie: În propoziția DELETE de pe customer. Dacă un trigger pornit de cascadă eșuează, toată propoziția, inclusiv nivelurile cascadei, este dată înapoi.
    14. Enunț: CREATE INDEX email_index ON users (email_address). Se schimbă emailurile din rânduri?
       Capcană: „Indexul rescrie duplicatele.”
       Cheie: Se adaugă un index secundar în schemă. Valorile rămân, inclusiv duplicatele. Indexul nu este UNIQUE.
    15. Enunț: INSERT cu două emailuri identice în users, sub UNIQUE KEY unique_email_address. Ce vezi la sfârșitul propoziției pe InnoDB?
       Capcană: „Primul email rămâne, al doilea este sărit, fără eroare.”
       Cheie: Apare eroare și propoziția este dată înapoi. INSERT IGNORE sare peste încălcarea de UNIQUE sau PRIMARY KEY. Pagina deschisă nu spune că IGNORE sare peste FOREIGN KEY.
    16. Enunț: Serverul a creat singur un index pe sales.customer_id pentru FOREIGN KEY. Mai ai nevoie de CONSTRAINT?
       Capcană: „Indexul automat este constrângerea.”
       Cheie: Indexul face verificarea rapidă. Regula este FOREIGN KEY. Indexul poate fi abandonat mai târziu dacă alt index poate impune constrângerea.
    17. Enunț: SET foreign_key_checks = 0, apoi INSERT cu 999. Ce vezi în SHOW CREATE TABLE și în sales?
       Capcană: „FOREIGN KEY a fost ștearsă din dicționar.”
       Cheie: Constrângerea rămâne în dicționar. Verificarea este oprită pe sesiune, iar rândul orfan rămâne. Așa încarcă și un dump, ca să creeze tabelele în orice ordine.
    18. Enunț: FOREIGN KEY fără ON DELETE, există o vânzare cu customer_id 7. DELETE FROM customer WHERE customer_id = 7.
       Capcană: „Clauza lipsă înseamnă CASCADE.”
       Cheie: Lipsa clauzei este NO ACTION, echivalentă cu RESTRICT pe InnoDB. Ștergerea părintelui este respinsă imediat. Rândul customer și rândul sales rămân.
    19. Enunț: ON DELETE SET NULL, iar sales.customer_id este NOT NULL. Ce eșuează?
       Capcană: „Eșuează doar DELETE-ul, CREATE TABLE a reușit.”
       Cheie: SET NULL cere ca coloana copil să poată fi NULL. Altfel definiția nu are unde să scrie NULL.
    20. Enunț: Un SELECT din sales s-a întors la client. Mai ține definiția FOREIGN KEY un lacăt pe customer?
       Capcană: „Cheia străină ține părintele blocat cât timp aplicația are rezultatul.”
       Cheie: Definiția nu ține lacăt după ce rezultatul a plecat. Refuzul și ștergerea se văd pe INSERT, UPDATE sau DELETE.

Deosebire de mecanismul vecin:
    21. Enunț: De ce KEY customer_id nu este FOREIGN KEY, deși ambele folosesc customer_id?
       Capcană: „Sunt același obiect scris de două ori în CREATE TABLE.”
       Cheie: KEY nu caută customer. FOREIGN KEY caută. INSERT 999 eșuează doar când CONSTRAINT este prezent.
    22. Enunț: De ce UNIQUE (email_address) nu este FOREIGN KEY către o tabelă de emailuri?
       Capcană: „Orice coloană unică este o cheie străină.”
       Cheie: UNIQUE nu are REFERENCES. Nu există părinte. Refuză duplicatul nenul în users.
    23. Enunț: De ce PRIMARY KEY (purchase_number) nu leagă sales de customer?
       Capcană: „Cheia primară este relația.”
       Cheie: PRIMARY KEY identifică rândul în sales. Nu are REFERENCES customer. customer_id poate fi orfan.
    24. Enunț: De ce relația one-to-many din slide nu este o propoziție SQL?
       Capcană: „Scrii RELATIONSHIP one-to-many și serverul o impune.”
       Cheie: Unu-la-mulți este FOREIGN KEY fără UNIQUE pe sales.customer_id. Cuvântul din diagramă nu este în dicționar.
    25. Enunț: De ce CREATE SCHEMA nu implementează planul din Relationships.pdf?
       Capcană: „În MySQL schema înseamnă relația, deci CREATE SCHEMA desenează legăturile.”
       Cheie: Fizic, SCHEMA este sinonim cu DATABASE. Conceptual, schema este mulțimea de obiecte. Sinonimul creează baza goală.
    26. Enunț: De ce un VIEW peste sales și customer nu ține loc de FOREIGN KEY?
       Capcană: „VIEW-ul salvează join-ul, deci orfanul nu mai poate intra.”
       Cheie: VIEW-ul este interogarea salvată. Nu respinge INSERT-ul în sales. Rândul orfan se scrie în tabelă.
    27. Enunț: De ce scoaterea emailului într-o tabelă nu este deja FOREIGN KEY?
       Capcană: „Dacă faptul stă o singură dată, serverul a și verificat legătura.”
       Cheie: Forma normală ține faptul o dată. Fără FOREIGN KEY, customer_id din sales poate indica un customer șters.
    28. Enunț: De ce indexul din CREATE INDEX email_index nu este constrângerea UNIQUE?
       Capcană: „Orice index este o constrângere.”
       Cheie: Constrângerea refuză rândul. KEY singur nu refuză duplicatul. UNIQUE este și index, și constrângere.
    29. Enunț: De ce ON DELETE CASCADE nu golește sales?
       Capcană: „CASCADE șterge toate vânzările.”
       Cheie: CASCADE șterge rândurile copil care se potrivesc cu părintele șters. Celelalte vânzări rămân.
    30. Enunț: De ce unu-la-unu nu este PRIMARY KEY pe sales?
       Capcană: „O cheie primară înseamnă unu-la-unu cu customer.”
       Cheie: PRIMARY KEY pe purchase_number face fiecare vânzare unică. Unu-la-unu cu customer este FOREIGN KEY plus UNIQUE (customer_id).

Eșec în producție:
    31. Enunț: Aplicația verifică customer înainte de INSERT, fără FOREIGN KEY. Ce se rupe?
       Capcană: „Verificarea din aplicație este aceeași regulă.”
       Cheie: Două sesiuni pot trece de verificare, sau un DELETE pe customer poate trece după verificare. FOREIGN KEY ține regula pe propoziția de scriere.
    32. Enunț: foreign_key_checks a rămas 0 după import. Ce rămâne?
       Capcană: „Setarea ține doar pe durata fișierului de dump, în sesiunea voastră de lucru.”
       Cheie: Este variabilă de sesiune. Dacă sesiunea continuă, INSERT-ul orfan trece, iar SHOW CREATE TABLE încă arată sales_ibfk_1.
    33. Enunț: FOREIGN KEY către o coloană părinte care nu este unică, fără restrict_fk_on_non_standard_key. Ce se întâmplă la creare?
       Capcană: „MySQL acceptă orice index părinte și asta rămâne recomandarea.”
       Cheie: Referința către un index părinte care nu este unic este învechită. Fără restrict_fk_on_non_standard_key, crearea nu rămâne calea de urmat. Coloanele referite trebuie să fie primele în index, în aceeași ordine.
    34. Enunț: ON DELETE SET DEFAULT în CREATE TABLE pe InnoDB.
       Capcană: „SET DEFAULT scrie valoarea implicită în sales la ștergerea customer-ului.”
       Cheie: Parserul recunoaște SET DEFAULT. InnoDB și NDB resping definiția tabelei.
    35. Enunț: customer și sales au motoare diferite. Unde eșuează FOREIGN KEY?
       Capcană: „Eroarea este la INSERT, tabela copil s-a creat.”
       Cheie: Părintele și copilul trebuie să folosească același motor și nu pot fi tabele temporare. Crearea constrângerii eșuează.
    36. Enunț: Cascadă pe mai mult de 15 niveluri.
       Capcană: „Se oprește la nivelul 15 și păstrează restul șters.”
       Cheie: Propoziția eșuează cu ER_FK_DEPTH_EXCEEDED.
    37. Enunț: INSERT IGNORE în sales cu customer_id orfan și cu FOREIGN KEY.
       Capcană: „IGNORE sare peste orice constrângere, deci orfanul este sărit.”
       Cheie: IGNORE sare peste încălcări de PRIMARY KEY sau UNIQUE. Pagina nu spune că sare peste FOREIGN KEY.
    38. Enunț: Fiecare dintre cele două tabele este și părinte și copil, iar CASCADE este doar pe o parte.
       Capcană: „O singură clauză CASCADE ajunge pentru ambele sensuri.”
       Cheie: CASCADE trebuie pe ambele FOREIGN KEY, altfel cascada eșuează.
    39. Enunț: customer_id este INT la copil și BIGINT la părinte.
       Capcană: „Întregii sunt suficient de apropiați pentru FOREIGN KEY.”
       Cheie: La tipurile de precizie fixă, mărimea și semnul trebuie să fie aceleași. Altfel constrângerea nu se creează. La șiruri, lungimea poate diferi, dar setul de caractere și colaționarea trebuie să coincidă.
    40. Enunț: Aplicația șterge întâi vânzările, apoi customer, în două apeluri. Între ele apare o vânzare nouă.
       Capcană: „Ordinea din aplicație este tranzacția.”
       Cheie: Fără o singură propoziție care ține copiii, ștergerea părintelui eșuează când apare un copil nou, sau copilul nou rămâne orfan dacă FOREIGN KEY lipsește. ON DELETE CASCADE face ștergerea în aceeași propoziție.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: Când lași verificarea în aplicație și nu pui FOREIGN KEY?
       Capcană: „Niciodată. Constrângerea este mereu mai ieftină.”
       Cheie: La încărcarea în care ordinea nu respectă părintele, sesiunea de import setează foreign_key_checks = 0, apoi verificarea revine. Pentru o tabelă de lucru în care orfanul este permis, alegi KEY customer_id. Pentru scrierea în care orfanul este o eroare, constrângerea stă pe server.
    42. Enunț: Când alegi UNIQUE și nu FOREIGN KEY?
       Capcană: „UNIQUE este varianta scurtă a FOREIGN KEY.”
       Cheie: UNIQUE (email_address) când duplicatul nenul este eroarea și nu există tabelă părinte. FOREIGN KEY când valoarea trebuie să existe în customer.
    43. Enunț: Când alegi index obișnuit și nu UNIQUE pe sales.customer_id?
       Capcană: „Indexul unic este mereu mai bun, pentru că și caută, și interzice.”
       Cheie: Unu-la-mulți cere repetiția: același client are multe vânzări. UNIQUE ar respinge a doua vânzare.
    44. Enunț: Customers către Sales în curs este one-to-many. Când nu scrii încă many-to-many?
       Capcană: „Pui customer_id și customer_id_2 în sales.”
       Cheie: Un customer_id din customer apare de multe ori în sales. A doua coloană nu este many-to-many. Many-to-many cere o tabelă de legătură cu două FOREIGN KEY.
    45. Enunț: Unde lași regula, într-un trigger AFTER DELETE pe customer sau în FOREIGN KEY?
       Capcană: „Trigger-ul șterge sales, deci FOREIGN KEY este opțional.”
       Cheie: Implicit, acțiunile cascade ale cheii străine nu pornesc trigger-e. Un INSERT orfan nu trece prin trigger-ul de DELETE. enable_cascade_triggers contează pentru tratarea din stratul SQL când innodb_native_foreign_keys este OFF și nu are efect când este ON.
    46. Enunț: Indexul de pe customer_id este invizibil. Mai verifică FOREIGN KEY?
       Capcană: „Dacă optimizerul nu vede indexul, nici constrângerea nu mai verifică.”
       Cheie: Un index invizibil nu este folosit la plan. FOREIGN KEY rămâne regula de scriere.
    47. Enunț: sales este mică. Mai adaugi un al doilea index pe customer_id doar ca să grăbești căutarea?
       Capcană: „Orice coloană din WHERE primește index.”
       Cheie: Când citirea atinge majoritatea rândurilor, citirea secvențială este mai rapidă. FOREIGN KEY poate avea deja indexul de verificare. Al doilea index pe aceeași coloană nu este a doua regulă.
    48. Enunț: Vânzările trebuie să rămână, cu customer_id NULL, când customer este șters. Ce scrii?
       Capcană: „ON DELETE CASCADE, apoi aplicația pune NULL.”
       Cheie: ON DELETE SET NULL, iar customer_id trebuie să permită NULL. CASCADE ar șterge vânzările. NO ACTION ar respinge ștergerea customer-ului.
    49. Enunț: Un customer nu are voie să aibă două vânzări, iar purchase_number rămâne identitatea vânzării. Ce adaugi?
       Capcană: „Schimbi PRIMARY KEY din purchase_number în customer_id.”
       Cheie: ADD UNIQUE KEY one_customer_once (customer_id). A doua vânzare cu același customer nenul este respinsă. PRIMARY KEY pe customer_id ar schimba identitatea rândului și indexul grupat InnoDB.
    50. Enunț: Mâine ai bilet și călător, fără sales și customer. Ce obiect pui ca biletul să nu rămână după ștergerea călătorului, dacă biletul trebuie să dispară odată cu el?
       Capcană: „Un index pe passenger_id și verificarea în serviciu.”
       Cheie: FOREIGN KEY (passenger_id) REFERENCES passenger (passenger_id) ON DELETE CASCADE. Dacă biletul trebuie să rămână fără călător, ON DELETE SET NULL și coloană nulabilă. Indexul singur lasă biletul orfan.
*/

// Începător: pachetul ține banca de interviu a schemei, fără să scrie în sales.
// Producție: package _5_Relational_Schema.fast. Fără el, cele 50 de întrebări nu stau lângă lecție.
// Expert: pachetul nu pornește un recall și nu deschide foreign_key_checks.
package _5_Relational_Schema.fast;

/*
🟢 Începător
    Ce este            cincizeci de întrebări despre alegerea dintre FOREIGN KEY, UNIQUE și index pe sales, customer și users
    De ce e nevoie     fără ele, KEY customer_id este luat drept sales_ibfk_1
🟡 Producție
    Cum se scrie       nu există main. Cheia este scrisă lângă fiecare enunț. Propoziția minimă este în RelationalSchema_Fast.childMustHaveParent
🔴 Staff
    Unde               recunoașterea vine din SHOW CREATE TABLE, din EXPLAIN sau din refuzul INSERT-ului
Pași                   1. zece recunoașteri
                       2. zece efecte pe rând, index și moment
                       3. zece deosebiri
                       4. zece eșecuri
                       5. zece alegeri
*/
// Începător: clasa ține întrebările și cheile. Nu are main care să le recite.
// Producție: public class RelationalSchema_Staff_Interview. Corpul este gol. Fără clasă, banca nu compilează ca fișier al schemei.
// Expert: un main de recall ar arăta cheia înainte de încercare. Cheia rămâne în comentariul de deasupra, nu într-o rulare.
public class RelationalSchema_Staff_Interview {
// Începător: banca se închide fără să creeze customer, sales sau users.
// Producție: fără această acoladă, cele 50 de întrebări nu stau într-o clasă care compilează.
// Expert: acolada nu execută INSERT IGNORE și nu schimbă innodb_native_foreign_keys. Regulile rămân cele din manualul 9.7 citit pentru sales_ibfk_1.
}
