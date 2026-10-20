// Începător: pachetul nu schimbă schema. Ține banca de interviu despre cuvântul rezervat.
// Producție: package _3_SQL_Keywords.fast. Fără linie, cele 50 de întrebări nu mai stau lângă topic.
// Expert: pachetul nu este INFORMATION_SCHEMA.KEYWORDS și nu are main de recall.
package _3_SQL_Keywords.fast;

/*
TOPIC:
    Cuvântul rezervat necotat este sintaxă: ERROR 1064, obiectul nu apare. Citarea sau punctul îl fac identificator.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Recunoști cuvântul din SQL sau din 1064, spui ce rămâne în schemă și îl deosebești de BEGIN, de ADD ca sintaxă și de funcția COUNT.
MECHANISM:
    Parserul, înainte de lacăt și de index. Cheile sunt mai jos. Corpul clasei nu le execută.
LOOKALIKES:
    BEGIN nerezervat, etichetă care tot se citează, TRUNCATE ca nume față de TRUNCATE TABLE, COMMIT ca nume față de propoziția COMMIT.
BOOK THEORY:
    Manualul 9.7: rezervat față de nerezervat, backtick, nume calificat, KEYWORDS.RESERVED, etichete, roluri, LIBRARY și EXTERNAL noi, MANUAL și PARALLEL nerezervate din 9.7.2, _FILENAME rezervat, replicarea parsează înainte de filtru.
BOOK EXAMPLE:
    SQL_Keywords.rejectedInterval
REPO DEMO:
    src/main/java/_3_SQL_Keywords/fast/SqlKeywords_Staff_Interview.java
BREAK LAB:
    CREATE TABLE interval (begin INT, end INT)
TRADE-OFFS:
    Redenumire în aplicație, citare când domeniul cere cuvântul, calificare doar cât rămâne punctul.
ANTI-PATTERNS:
    Listă de cuvinte înghețată din 8.4, sau filtru de replicare folosit în loc de citare.
STAFF QUESTIONS:
    cele 50 de mai jos, fiecare cu demonstrația din SQL_Keywords
NOVEL TRANSFER:
    CREATE TABLE order (purchase_number INT) pe un domeniu de comenzi.
SPACED REVIEW:
    o zi: 1-10. șapte zile: 11-30. treizeci de zile: 31-50, pe un nume care nu este interval.
*/

/*
Definiția unui cuvânt cheie rezervat (reserved keyword):
       Necotat, în locul identificatorului, dă ERROR 1064 și obiectul nu apare.

Exemplu:
       SqlKeywords_Terms.reservedKeyword()
       CREATE TABLE interval nu lasă tabelă.

Definiția unui identificator (identifier):
       Numele obiectului. Rezervatul devine identificator între backtick-uri sau după punct.

Exemplu:
       SqlKeywords_Terms.identifier()
       `interval` lasă 0 rânduri.

Definiția unui cuvânt cheie nerezervat (nonreserved keyword):
       Poate fi identificator fără citare. BEGIN și END sunt așa în 9.7.

Exemplu:
       SqlKeywords_Terms.nonreservedKeyword()
       Coloana begin nu cere backtick.

Definiția unui nume calificat (qualified name):
       După punct, cuvântul rezervat nu se citează.

Exemplu:
       SqlKeywords_Terms.qualifiedName()
       mydb.interval.

Definiția unui cuvânt cheie restricționat (restricted keyword):
       Nerezervat, dar citat ca etichetă sau ca rol. BEGIN ca etichetă. SUPER ca rol.

Exemplu:
       SqlKeywords_Terms.restrictedKeyword()
       RESERVED 0 nu scutește eticheta.

Definiția unei tabele (table):
       Apare doar dacă numele trece de parser.

Exemplu:
       SqlKeywords_Terms.tableName()
       `interval` are 0 rânduri.

Definiția unei coloane (column):
       begin poate fi necotată. rank necotat nu.

Exemplu:
       SqlKeywords_Terms.columnName()
       ADD COLUMN rank cade cu 1064.
*/

/*
10 recunoaștere din SQL, EXPLAIN sau eroare

1. Enunț: Primești ERROR 1064 (42000) near 'interval (begin INT, end INT)' după CREATE TABLE interval (begin INT, end INT). Ce cuvânt a oprit parserul și ce tabelă există?
   Capcană: Eroarea spune că lipsește tabela interval, deci o creezi din nou la fel.
   Cheie: INTERVAL este rezervat. 1064 este la parsare și nu rămâne nicio tabelă. Nu retrimiți aceeași propoziție.
   Demonstrație: SQL_Keywords.rejectedInterval
2. Enunț: CREATE TABLE `interval` (begin INT, end INT) răspunde Query OK, 0 rows affected. Ce obiect există și câte rânduri are?
   Capcană: 0 rows affected înseamnă că CREATE a eșuat în tăcere.
   Cheie: Backtick-ul a făcut identificator din INTERVAL. Tabela există și este goală. Nu este un DELETE.
   Demonstrație: SQL_Keywords.quotedInterval
3. Enunț: CREATE TABLE mydb.interval (begin INT, end INT) trece fără backtick. De ce interval de după punct nu cere citare?
   Capcană: Punctul dezactivează cuvintele rezervate în toată propoziția.
   Cheie: După punct, cuvântul trebuie să fie identificator, deci rezervatul nu se citează. Înainte de punct, regula nu se schimbă.
   Demonstrație: SQL_Keywords.qualifiedInterval
4. Enunț: SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'INTERVAL' întoarce 1. Ce decizie iei înainte de CREATE?
   Capcană: 1 înseamnă că și forma cu backtick este interzisă.
   Cheie: RESERVED 1 înseamnă că forma necotată cade cu 1064. Citarea rămâne legală. SELECT-ul nu creează tabela.
   Demonstrație: SQL_Keywords.reservedFlag
5. Enunț: În lista 9.7, BEGIN nu are (R). Poți scrie CREATE TABLE sales (purchase_number INT, begin INT)?
   Capcană: BEGIN deschide tranzacția, deci coloana este respinsă ca INTERVAL.
   Cheie: BEGIN este nerezervat. Coloana trece necotată. Ca etichetă de program stocat, același cuvânt se citează.
   Demonstrație: SQL_Keywords.beginColumnOnSales
6. Enunț: CREATE TABLE alter (purchase_number INT). ALTER este (R). Ce eroare vezi și ce tabelă rămâne?
   Capcană: ALTER din nume creează tabela și așteaptă un al doilea ALTER ca să o schimbe.
   Cheie: În poziția numelui, ALTER dă 1064 și tabela nu apare. Nu s-a adăugat date_of_purchase.
   Demonstrație: SQL_Keywords.alterAsTableName
7. Enunț: ALTER TABLE sales ADD COLUMN date_of_purchase DATE folosește două cuvinte (R) și tot este validă. Unde stau cuvintele?
   Capcană: Orice apariție a lui ALTER sau ADD este interzisă, deci propoziția nu poate trece.
   Cheie: Aici sunt sintaxă, nu identificatori. Coloana date_of_purchase apare. Nu se inserează un rând.
   Demonstrație: SQL_Keywords.addPurchaseDate
8. Enunț: Cineva cere EXPLAIN pe CREATE TABLE interval (begin INT, end INT). Există un plan de citit?
   Capcană: EXPLAIN arată de ce InnoDB a refuzat rândul.
   Cheie: 1064 oprește propoziția înainte să existe un plan. Nu este un index nefolosit. Nu este un lacăt de văzut în EXPLAIN.
   Demonstrație: SQL_Keywords.rejectedInterval
9. Enunț: CREATE TABLE sales (count INT). COUNT este nume de funcție. Este cuvânt rezervat în poziția coloanei?
   Capcană: COUNT este interzis ca identificator, la fel ca SELECT.
   Cheie: Manualul 9.7 permite COUNT ca nume de coloană. Apelul este COUNT( fără spațiu. Acest CREATE nu numără rânduri.
   Demonstrație: SQL_Keywords.countColumn
10. Enunț: Pe 9.7.2, lângă MANUAL scrie became nonreserved. CREATE TABLE manual (purchase_number INT) mai este respins?
    Capcană: O dată rezervat, rămâne rezervat pe toată seria 9.7, inclusiv 9.7.2.
    Cheie: Din 9.7.2 MANUAL nu mai este rezervat, deci CREATE trece fără backtick. PARALLEL s-a schimbat la fel, dar nu este acest cuvânt. RANK nu s-a schimbat.
    Demonstrație: SQL_Keywords.manualTable

10 rânduri, index, lacăt și momentul în care se vede efectul

11. Enunț: După 1064 pe CREATE TABLE interval, câte tabele noi vezi în schemă?
    Capcană: Tabela există, dar este goală, pentru că eroarea a șters rândurile.
    Cheie: Zero tabele noi. Eșecul este la parsare, înainte să se scrie obiectul.
    Demonstrație: SQL_Keywords.rejectedInterval
12. Enunț: După Query OK pe CREATE TABLE `interval` (begin INT, end INT), ce conține tabela?
    Capcană: begin și end sunt rânduri inserate de CREATE.
    Cheie: Sunt coloane. Tabela are 0 rânduri. Momentul este sfârșitul propoziției DDL, nu un INSERT.
    Demonstrație: SQL_Keywords.quotedInterval
13. Enunț: 1064 pe interval necotat ține un lacăt de rând pe sales?
    Capcană: Orice eroare SQL ține lacăt până la ROLLBACK.
    Cheie: Parserul cade înainte de InnoDB. Nu există lacăt de rând de eliberat, pentru că nu s-a deschis schimbarea.
    Demonstrație: SQL_Keywords.rejectedInterval
14. Enunț: Se construiește un index când CREATE TABLE interval cade cu 1064?
    Capcană: InnoDB a început indexul grupat și l-a lăsat pe jumătate.
    Cheie: Nu. Nu există tabelă, deci nu există index. Citarea din quotedInterval creează tabela goală, iar indexul ei nu este acest eșec.
    Demonstrație: SQL_Keywords.rejectedInterval
15. Enunț: Când se vede eșecul lui interval necotat: la commit, sau mai devreme?
    Capcană: Aștepți COMMIT ca să știi dacă tabela a rămas.
    Cheie: Se vede la parsare, ca ERROR 1064, înainte de commit. Nu există o schimbare pe care COMMIT să o facă durabilă.
    Demonstrație: SQL_Keywords.rejectedInterval
16. Enunț: ALTER TABLE sales ADD COLUMN date_of_purchase DATE schimbă rândurile sau schema?
    Capcană: ADD COLUMN inserează un rând cu data cumpărării.
    Cheie: Adaugă coloana. Rândurile care existau rămân, fără un rând nou. Efectul se vede la sfârșitul propoziției, dacă sales există.
    Demonstrație: SQL_Keywords.addPurchaseDate
17. Enunț: Backtick-ul din CREATE TABLE `interval` actualizează rândurile unei tabele deja pline cu același nume?
    Capcană: Citarea face UPDATE pe interval.
    Cheie: CREATE citat creează obiectul gol. Nu este UPDATE. Dacă numele există deja, eșecul de după parsare este obiect duplicat, nu 1064.
    Demonstrație: SQL_Keywords.quotedInterval
18. Enunț: SELECT din INFORMATION_SCHEMA.KEYWORDS pentru INTERVAL schimbă schema?
    Capcană: Citirea steagului rezervă cuvântul și blochează CREATE până la commit.
    Cheie: Este citire. RESERVED rămâne 1 sau 0. Nu apare tabelă și nu se ia lacăt pe sales.
    Demonstrație: SQL_Keywords.reservedFlag
19. Enunț: Propoziția următoare din aceeași sesiune vede tabela interval după 1064?
    Capcană: Eroarea a fost doar un avertisment, iar tabela este vizibilă după.
    Cheie: Nu există tabelă de văzut. Următorul SELECT begin FROM interval cade iar la parser dacă rămâne necotat.
    Demonstrație: SQL_Keywords.rejectedInterval
20. Enunț: CREATE TABLE sales (purchase_number INT, begin INT) lasă coloana begin doar dacă numele tabelei trece. Ce se întâmplă dacă înlocuiești sales cu interval?
    Capcană: Coloana begin se creează oricum, iar tabela cade după.
    Cheie: Parserul cade pe INTERVAL înainte să apară coloana. BEGIN nerezervat nu salvează un nume de tabelă rezervat.
    Demonstrație: SQL_Keywords.beginColumnOnSales

10 deosebire de mecanismul vecin

21. Enunț: Cu ce condiție begin este coloană legală, iar interval nu este nume de tabelă necotat?
    Capcană: Ambele sunt keywords, deci ambele se citează la fel.
    Cheie: RESERVED este 0 pentru BEGIN și 1 pentru INTERVAL. Citarea este obligatorie doar la 1, în poziție de identificator necotat.
    Demonstrație: SQL_Keywords.beginColumnOnSales
22. Enunț: De ce CREATE TABLE alter (purchase_number INT) nu este ALTER TABLE sales ADD COLUMN date_of_purchase DATE?
    Capcană: Ambele „fac alter”, deci ambele adaugă coloana.
    Cheie: Prima pune ALTER în poziția numelui și dă 1064, fără tabelă. A doua pune ALTER și ADD ca sintaxă și adaugă coloana.
    Demonstrație: SQL_Keywords.alterAsTableName
23. Enunț: De ce CREATE TABLE sales (count INT) nu este apelul COUNT(purchase_number)?
    Capcană: COUNT înseamnă mereu agregare, deci CREATE numără rândurile din sales.
    Cheie: Fără ( lipit de nume, COUNT este coloană. Agregarea este cealaltă scriere și nu este acest CREATE.
    Demonstrație: SQL_Keywords.countColumn
24. Enunț: BEGIN ca coloană și BEGIN ca etichetă de program stocat sunt același drept de a nu cita?
    Capcană: RESERVED 0 scutește cuvântul în orice context.
    Cheie: Coloana din beginColumnOnSales trece necotată. Lista restricționată din 9.7 cere citare când BEGIN este etichetă. Nu este același loc.
    Demonstrație: SQL_Keywords.beginColumnOnSales
25. Enunț: SUPER trebuie citat ca nume de rol. Înseamnă că SUPER este rezervat ca INTERVAL?
    Capcană: Orice cuvânt care se citează undeva are RESERVED 1 și nu poate fi tabelă.
    Cheie: Lista de roluri este o restricție de context, nu steagul (R) de pe INTERVAL. Decizia de tabelă se citește din KEYWORDS.RESERVED, nu din lista de roluri.
    Demonstrație: SQL_Keywords.reservedFlag
26. Enunț: CREATE TABLE truncate (purchase_number INT) golește sales?
    Capcană: Cuvântul TRUNCATE șterge rândurile de fiecare dată când apare.
    Cheie: TRUNCATE nu este (R), deci poate fi nume și acest CREATE nu șterge rânduri. TRUNCATE TABLE ar goli o tabelă și nu este această propoziție. Ca etichetă, TRUNCATE se citează.
    Demonstrație: SQL_Keywords.truncateAsName
27. Enunț: CREATE TABLE commit (purchase_number INT) face durabilă tranzacția deschisă?
    Capcană: Numele commit este propoziția COMMIT.
    Cheie: COMMIT nu este rezervat, deci poate fi tabelă. Durabilitatea este propoziția COMMIT, singură, nu identificatorul. ROLLBACK rămâne celălalt sfârșit.
    Demonstrație: SQL_Keywords.commitAsName
28. Enunț: mydb.interval și `interval` creează ambele tabela. Care este condiția care le separă?
    Capcană: Sunt sinonime, deci scoți mydb. și lași interval necotat.
    Cheie: Fără punct, interval necotat dă 1064. Punctul este excepția. Backtick-ul este cealaltă excepție, când numele stă singur.
    Demonstrație: SQL_Keywords.qualifiedInterval
29. Enunț: PDF-ul spune că un keyword nu poate fi nume de obiect. Unde pică afirmația pe BEGIN?
    Capcană: PDF-ul este regula serverului 9.7, deci și begin se citează.
    Cheie: Manualul limitează interdicția la rezervat necotat. BEGIN este keyword și tot poate fi coloana din beginColumnOnSales. ALTER necotat ca nume rămâne interzis, cum arată PDF-ul cu CREATE TABLE alter.
    Demonstrație: SQL_Keywords.beginColumnOnSales
30. Enunț: GRANT este (R). Ce privilegiu se schimbă dacă îl pui coloană necotată, ca în poziția în care ALTER eșuează ca nume?
    Capcană: Coloana grant acordă dreptul și abia apoi devine nume.
   Cheie: CREATE TABLE sales (grant INT) pune GRANT rezervat în poziția coloanei, dă 1064 și nu schimbă privilegiile. GRANT ca propoziție DCL este alt loc în text.
   Demonstrație: SQL_Keywords.grantAsColumn

10 eșec în producție

31. Enunț: O sursă veche are coloana rank. Replica 9.7 primește ALTER TABLE sales ADD COLUMN rank INT. De ce cade chiar dacă tabela este în replicate-ignore-table?
    Capcană: Filtrul sare peste text înainte de parsare, deci rank rezervat nu ajunge să fie văzut.
    Cheie: RANK este rezervat din 8.0. Replica parsează evenimentul înainte de filtrele do/ignore și dă 1064. Coloana nu apare.
    Demonstrație: SQL_Keywords.unquotedRank
32. Enunț: --replicate-ignore-db pe schema comenzilor salvează un eveniment care conține order necotat?
    Capcană: Baza ignorată nu este parsată.
    Cheie: Parserul replicii vede cuvântul rezervat înainte de filtru. Ignorarea bazei nu este citarea. Același 1064 ca la rank necotat.
    Demonstrație: SQL_Keywords.unquotedOrder
33. Enunț: Aplicația concatenează numele de tabelă interval fără backtick. Ce ajunge pe server?
    Capcană: Driverul JDBC citează automat orice cuvânt din lista ta Java.
    Cheie: Connector/J trimite textul. CREATE TABLE interval cade cu 1064 și nu creează tabela. Lista din aplicație nu este parserul.
    Demonstrație: SQL_Keywords.rejectedInterval
34. Enunț: Un CREATE TABLE library (purchase_number INT) scris pe 8.4 ajunge pe 9.7. Ce s-a schimbat în listă?
    Capcană: 9.7 a șters LIBRARY din keywords, deci numele trece mai ușor.
    Cheie: LIBRARY a devenit rezervat în 9.7. Între 8.4 și 9.7 nu s-a scos niciun cuvânt. CREATE necotat dă 1064 și tabela nu apare. EXTERNAL este cealaltă rezervare nouă.
    Demonstrație: SQL_Keywords.libraryTable
35. Enunț: Un test de pe 9.7.0 așteaptă 1064 la CREATE TABLE manual. Rulează pe 9.7.2. De ce pică testul?
    Capcană: Patch-ul nu schimbă niciodată un cuvânt rezervat.
    Cheie: MANUAL a devenit nerezervat în 9.7.2, deci CREATE trece. Testul care așteaptă 1064 este fals pe acest patch. PARALLEL s-a schimbat la fel.
    Demonstrație: SQL_Keywords.manualTable
36. Enunț: O procedură folosește begin ca etichetă, necotat, deși coloana begin din sales este legală. Care formă cade?
    Capcană: Dacă coloana trece, și eticheta trece.
    Cheie: Lista din 9.7 cere citare pentru BEGIN ca etichetă. Coloana din beginColumnOnSales nu este acea etichetă. Eșecul este la etichetă, nu la coloană.
    Demonstrație: SQL_Keywords.beginColumnOnSales
37. Enunț: Cineva creează rolul SUPER fără citare, pentru că nu l-a văzut cu (R) lângă INTERVAL. Ce listă a sărit?
    Capcană: Doar cuvintele (R) se citează, deci rolul SUPER este un identificator liber.
    Cheie: SUPER este în lista care trebuie citată ca nume de rol. Nu este aceeași listă cu RESERVED 1 de pe INTERVAL. reservedFlag nu îți spune rolul.
    Demonstrație: SQL_Keywords.reservedFlag
38. Enunț: EXECUTE, RESTART și SHUTDOWN sunt folosite necotat și ca etichetă, și ca rol. Ajunge RESERVED 0?
    Capcană: Fără (R), cuvântul este liber în ambele contexte.
    Cheie: Manualul 9.7 le pune la cuvinte care se citează și ca etichetă, și ca rol. Steagul 0 nu acoperă cele două contexte. Nu este cazul coloanei begin.
    Demonstrație: SQL_Keywords.reservedFlag
39. Enunț: CREATE TABLE _filename (purchase_number INT). Cuvântul nu are (R) în alfabet. De ce cade?
    Capcană: Dacă nu este în lista cu (R), parserul îl ia ca identificator.
    Cheie: Manualul spune separat că _FILENAME este rezervat. 1064, tabela nu apare. Nu este calea fișierului de date.
    Demonstrație: SQL_Keywords.filenameTable
40. Enunț: SELECT COUNT (purchase_number) FROM sales are un spațiu înainte de (. Este agregarea?
    Capcană: Spațiul este doar stil, iar COUNT tot numără.
    Cheie: Manualul nu permite spațiu între numele funcției și (. Nu este apelul built-in și nu este un cuvânt rezervat în poziție de tabelă. Nu da un cod de eroare pe care nu l-ai văzut.
    Demonstrație: SQL_Keywords.countWithSpace

10 trade-off, inclusiv când rămâi în aplicație

41. Enunț: Domeniul poate schimba numele interval. Unde oprești cuvântul rezervat?
    Capcană: Îl trimiți necotat și prinzi 1064 ca flux normal.
    Cheie: Aplicația nu trimite rejectedInterval. Alege un nume nerezervat, ca sales, înainte de server. 1064 nu este o validare de formular.
    Demonstrație: SQL_Keywords.rejectedInterval
42. Enunț: Citezi fiecare keyword, inclusiv begin, ca să fii acoperit?
    Capcană: Backtick pe orice cuvânt din KEYWORDS este obligatoriu.
    Cheie: Citezi când RESERVED este 1. BEGIN are 0 și coloana rămâne necotată. Citarea universală ascunde diferența.
    Demonstrație: SQL_Keywords.beginColumnOnSales
43. Enunț: Te bazezi pe mydb.interval ca să nu mai pui backtick niciodată. Când se rupe?
    Capcană: O dată calificat, cuvântul rămâne legal și fără punct.
    Cheie: Excepția ține doar cât punctul este în propoziție. Fără mydb., interval necotat dă 1064. qualifiedInterval nu înlocuiește quotedInterval.
    Demonstrație: SQL_Keywords.qualifiedInterval
44. Enunț: Aplicația ține lista de cuvinte rezervate copiată de pe 8.4. Ce ratează pe 9.7?
    Capcană: Lista nu crește între LTS-uri, deci library trece.
    Cheie: LIBRARY și EXTERNAL sunt rezervate noi. CREATE TABLE library cade cu 1064. Sursa vie este KEYWORDS de pe instanță, nu copia veche.
    Demonstrație: SQL_Keywords.libraryTable
45. Enunț: Aplicația adaugă date_of_purchase doar în memorie și spune că schema s-a schimbat. Unde este schimbarea reală?
    Capcană: Obiectul Java este coloana, deci ALTER nu mai este necesar.
    Cheie: Coloana apare când serverul termină ALTER TABLE sales ADD COLUMN date_of_purchase DATE. Fără propoziție, sales nu are coloana. Nu este un rând nou.
    Demonstrație: SQL_Keywords.addPurchaseDate
46. Enunț: Ai citat interval doar în CREATE. SELECT begin, end FROM interval ce face?
    Capcană: Citarea din CREATE se păstrează în dicționar și scutește SELECT-ul.
    Cheie: Fiecare propoziție se parsează singură. SELECT trebuie să scrie FROM `interval`. Altfel 1064, fără plan.
    Demonstrație: SQL_Keywords.selectFromQuotedInterval
47. Enunț: Pe replicare, rank trebuie să rămână numele coloanei. Filtrul sau citarea?
    Capcană: Ignori tabela și evenimentul necotat trece.
    Cheie: quotedRank trece de parser. Filtrul nu ajunge să ruleze dacă textul necotat cade la parsare. Reparația este pe sursă sau în textul citat.
    Demonstrație: SQL_Keywords.quotedRank
48. Enunț: Aplicația întreabă KEYWORDS.RESERVED înainte de DDL. Mai poate apărea 1064?
    Capcană: Dacă steagul este 0 în client, serverul nu mai are cum să respingă.
    Cheie: Verificarea din reservedFlag reduce plecarea. Parserul instanței rămâne autoritatea: alt patch, ca MANUAL în 9.7.2, poate schimba steagul între citire și CREATE.
    Demonstrație: SQL_Keywords.reservedFlag
49. Enunț: Domeniul de comenzi cere tabela order. Ce alegi față de un nume pe care îl poți schimba?
    Capcană: ORDER BY rezolvă și numele tabelei, deci CREATE TABLE order trece.
    Cheie: ORDER este rezervat. unquotedOrder dă 1064 și nu sortează nimic. quotedOrder lasă o tabelă goală. Dacă domeniul lasă alt nume, nu trimite rezervatul.
    Demonstrație: SQL_Keywords.quotedOrder
50. Enunț: Pe 9.7.2 lași un test care cere 1064 pentru MANUAL și PARALLEL, copiat dintr-o regulă de serie Innovation. Ce păstrezi?
    Capcană: Seria nouă Innovation este lista care comandă și LTS-ul 9.7.
    Cheie: Pentru instanța 9.7.2, MANUAL și PARALLEL sunt nerezervate, deci CREATE TABLE manual trece. Testul de 1064 este fals. Nu imporți lista Innovation peste manualul 9.7 citit.
    Demonstrație: SQL_Keywords.manualTable
*/

// Începător: clasa nu execută întrebările. Banca stă în comentariu, fără main.
// Producție: public class SqlKeywords_Staff_Interview. Fără clasă, cele 50 nu au fișierul cerut de fast.
// Expert: un main aici ar arăta cheia înainte de încercare. Recall-ul nu pornește din această clasă.
/*
🟢 Începător
    Ce este            50 de întrebări despre cuvântul rezervat, nu despre un alt topic
    De ce e nevoie     fără ele, interviul nu are cele cinci grupe
🟡 Producție
    Cum se scrie       comentariu, corp fără metode și fără main
🔴 Staff
    Unde               cheia lângă enunț, în fișier; la recall rămâne ascunsă
Pași                   1. 10 recunoașteri.
                       2. 10 efecte.
                       3. 10 vecini.
                       4. 10 eșecuri.
                       5. 10 alegeri.
*/
public class SqlKeywords_Staff_Interview {
    // Începător: acolada nu creează tabela interval și nu răspunde la întrebare.
    // Producție: închide SqlKeywords_Staff_Interview. Fără ea, banca nu compilează pe Java 27.
    // Expert: acolada nu este ERROR 1064 și nu înlocuiește o demonstrație din SQL_Keywords.
}
