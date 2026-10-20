/*
TOPIC:
    Cincizeci de întrebări despre celula din entity_row față de rândul de rezultat.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. root@localhost a primit Access denied. Efectul pe server rămâne NERULAT AICI. EXPLAIN nu a fost citit.
INTENT:
    Recunoaștere din INSERT sau din SELECT 1, momentul celulei, deosebirea de rezultat, eșecul fără tabelă și alegerea față de aplicație.
MECHANISM:
    INSERT INTO entity_row (data_value) VALUES (1) lasă celula dacă tabela există și propoziția se termină. SELECT 1 AS data_value nu o lasă.
LOOKALIKES:
    Rezultatul din _1_SQL. Cererea declarativă. CREATE TABLE, care face coloana fără valoare de date.
BOOK THEORY:
    Manualul 9.7, 1.2.1: colecție structurată, tabele separate, rânduri și coloane. Folderul nu are PDF.
BOOK EXAMPLE:
    Introduction_To_DB.storedDataValue și Introduction_To_DB.resultIsNotACell
REPO DEMO:
    src/main/java/_1_SQL/_1_Introduction_To_DB/Introduction_To_DB.java
BREAK LAB:
    SELECT 1 AS data_value
TRADE-OFFS:
    Aplicația când nimeni nu recitește valoarea. Declarativ când ceri citirea. Propozițiile când clasifici CREATE și INSERT.
ANTI-PATTERNS:
    Aliasul data_value tratat ca record.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main.
NOVEL TRANSFER:
    Stocul de pe raft față de cifra de pe scanner.
SPACED REVIEW:
    O zi: 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei valori de date (data value):
       Conținutul celulei, la întâlnirea rândului cu coloana.

Exemplu:
       IntroductionToDb_Terms.explainDataValue()

Definiția unui rând (row):
       Înregistrarea din tabelă, nu rândul de rezultat.

Exemplu:
       IntroductionToDb_Terms.explainRecord()

Definiția unei coloane (column):
       Atributul vertical. Fără rând, nu ține încă o valoare de date.

Exemplu:
       IntroductionToDb_Terms.explainColumn()

Definiția unei tabele (table):
       Locul rândurilor. Baza relațională le separă.

Exemplu:
       IntroductionToDb_Terms.explainTable()

🟢 Începător
    Ce este            cincizeci de întrebări despre celulă
    De ce e nevoie     fără cheie, 1 de pe ecran trece drept stoc
🟡 Producție
    Cum se scrie       INSERT INTO entity_row (data_value) VALUES (1)
🔴 Staff
    Unde               celula rămâne în tabelă. Nu există main de recall.
Pași
    1. Recunoaște textul.
    2. Spune ce rămâne în entity_row.
    3. Desparte celula de SELECT 1.

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: În jurnal vezi INSERT INTO entity_row (data_value) VALUES (1). Ce rămâne dacă propoziția s-a terminat și tabela există?
       Capcană: Doar un mesaj de succes, valoarea trăiește în sesiune.
       Cheie: O celulă: rând nou, coloana data_value, conținutul 1. Efectul este în tabelă la sfârșitul propoziției. În tur jurnalul nu a fost citit: autentificarea a căzut.
    2. Enunț: Clientul a primit o coloană data_value cu 1, iar textul era SELECT 1 AS data_value. Ce recunoști?
       Capcană: Un INSERT prescurtat.
       Cheie: Un rând de rezultat. entity_row nu este numită. Nu există celulă nouă.
    3. Enunț: EXPLAIN nu a fost rulat. Din INSERT-ul de mai sus, ce index poți afirma că a fost folosit?
       Capcană: PRIMARY, fiindcă orice scriere trece prin cheie.
       Cheie: Nu afirmi un plan necitit. Textul nu numește un index. Celula nu depinde de un index ca să existe.
    4. Enunț: Eroarea spune că entity_row nu există, pe INSERT INTO entity_row (data_value) VALUES (1). Ce nu a apărut?
       Capcană: Tabela a fost creată pe jumătate și 1 este deja în ea.
       Cheie: Nici tabela, nici celula. INSERT nu este CREATE TABLE.
    5. Enunț: O metodă Java tipărește 1 și jurnalul serverului este gol. Există o celulă?
       Capcană: Da, pentru că valoarea a fost produsă.
       Cheie: Nu. Fără propoziție trimisă, serverul nu a scris. System.out nu este celula.
    6. Enunț: Comentariul din Introduction_To_DB spune că data value este la intersecția rândului cu coloana. Unde este propoziția?
       Capcană: Comentariul este propoziția.
       Cheie: Propoziția este INSERT-ul din storedDataValue. Comentariul nu ajunge la server.
    7. Enunț: Workbench arată o grilă cu o celulă. Unde este mecanismul?
       Capcană: În click-ul care a deschis grila.
       Cheie: În propoziția executată. Grila poate fi un rezultat, nu un record. Click-ul nu scrie rândul.
    8. Enunț: Vezi SELECT 1 AS data_value lângă INSERT INTO entity_row (data_value) VALUES (1). Care scrie celula?
       Capcană: Ambele, pentru că ambele spun data_value.
       Cheie: Doar INSERT-ul, și doar dacă entity_row există. SELECT 1 nu are INTO și nu are tabelă.
    9. Enunț: Jurnalul are textul INSERT-ului, nu și confirmația că s-a terminat. Când vezi celula?
       Capcană: În clipa în care textul a fost primit.
       Cheie: La sfârșitul propoziției, în tabelă. Textul din jurnal nu este celula.
    10. Enunț: Access denied pentru root@localhost apare înainte de INSERT. Ce celulă s-a scris?
        Capcană: INSERT-ul a rulat, apoi parola a oprit afișarea.
        Cheie: Niciuna. Refuzul este la conectare. storedDataValue nu a fost trimis.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: După ce INSERT-ul se termină, unde stă 1?
        Capcană: Doar în răspunsul clientului, până la COMMIT.
        Cheie: În rândul din entity_row, coloana data_value. Altă sesiune îl poate citi după ce scrierea este durabilă. În tur nu am văzut rândul.
    12. Enunț: Ce lacăt rămâne după ce INSERT-ul s-a terminat și tranzacția s-a închis?
        Capcană: Lacătul de rând rămâne cât timp 1 este în celulă.
        Cheie: Celula rămâne. Lacătul scrierii nu rămâne după închiderea tranzacției. Propoziția nu ține lacătul la nesfârșit.
    13. Enunț: Un index pe data_value face ca INSERT-ul să nu mai scrie celula?
        Capcană: Indexul înlocuiește rândul.
        Cheie: Indexul nu este celula. Rândul tot se scrie. Indexul poate încetini scrierea. Nu l-am măsurat în tur.
    14. Enunț: A doua sesiune citește entity_row după SELECT 1 AS data_value. Ce vede din acel SELECT?
        Capcană: Rândul cu 1.
        Cheie: Nimic din acea propoziție. Nu a scris în tabelă.
    15. Enunț: INSERT-ul stă într-o tranzacție deschisă și dai ROLLBACK. Ce celulă dispare?
        Capcană: Niciuna, pentru că INSERT-ul a fost deja vizibil pentru toți.
        Cheie: Celula scrisă de acel INSERT, dacă tranzacția nu a fost închisă cu COMMIT. În tur nu am deschis tranzacția.
    16. Enunț: autocommit este 1. Când devine celula durabilă?
        Capcană: Doar după un COMMIT scris de mână.
        Cheie: La sfârșitul propoziției, pentru că propoziția este propria tranzacție. Momentul nu este afișajul clientului.
    17. Enunț: Închizi clientul după ce a tipărit 1 din SELECT 1. Ce dispare din entity_row?
        Capcană: Celula data_value.
        Cheie: Nimic din acea propoziție, pentru că nu a scris. Se închide desenul clientului.
    18. Enunț: Când se vede celula: la primirea textului, la commit sau la sfârșitul INSERT-ului?
        Capcană: La primirea textului în jurnal.
        Cheie: La sfârșitul propoziției, în tabelă. Cu autocommit oprit, durabilitatea așteaptă COMMIT, iar celula este deja în tranzacție.
    19. Enunț: Poate SELECT 1 AS data_value să țină un lacăt pe entity_row?
        Capcană: Da, orice propoziție blochează toate tabelele.
        Cheie: Nu numește entity_row. Nu ai din acest text un lacăt pe tabela aceea.
    20. Enunț: rows_examined nu a fost citit. Câte rânduri stocate atinge SELECT 1 AS data_value?
        Capcană: Toate rândurile din instanță.
        Cheie: Niciunul. Nu numește o tabelă. Cifra din plan rămâne necitită.

Deosebire de mecanismul vecin:
    21. Enunț: De ce celula nu este SELECT 1 AS data_value?
        Capcană: Același nume data_value înseamnă același lucru.
        Cheie: Celula este conținut stocat. SELECT 1 este un rând de rezultat, fără tabelă.
    22. Enunț: De ce descrierea din comentariu nu ține loc de rând?
        Capcană: Intersecția este definită, deci valoarea există.
        Cheie: Definiția spune ce este o celulă. Conținutul apare când o propoziție îl scrie într-o tabelă care există.
    23. Enunț: De ce CREATE TABLE entity_row (data_value INT) nu este încă o valoare de date?
        Capcană: Coloana există, deci data value există.
        Cheie: Coloana fără rând nu are conținut. Data value este conținutul celulei. CREATE este clasificat în _2_SQL_Statements.
    24. Enunț: De ce SELECT data_value FROM entity_row nu înlocuiește INSERT-ul?
        Capcană: Citește data_value, deci este celula.
        Cheie: Citește ce este deja stocat. Nu adaugă celula. Dacă tabela e goală, rezultatul nu inventează un record.
    25. Enunț: O listă Java cu un 1 este un record?
        Capcană: Da, dacă o numești data_value.
        Cheie: Nu a trecut prin server. Record-ul este rândul din tabelă.
    26. Enunț: Un API ascunde SQL-ul și aplicția are o listă. A văzut tabela celula?
        Capcană: Da, pentru că manualul spune că API-ul poate ascunde sintaxa.
        Cheie: Doar dacă în spate a plecat o propoziție care scrie. Lista singură nu scrie entity_row.
    27. Enunț: De ce un click în Workbench nu dovedește un record nou?
        Capcană: Grila cu o celulă este recordul.
        Cheie: Dovada este propoziția care a scris. Grila poate fi un SELECT 1.
    28. Enunț: Șirul INSERT stă în Java și nu este trimis. S-a scris celula?
        Capcană: Compilarea a executat-o.
        Cheie: Nu. Serverul scrie doar ce primește. În tur a refuzat conexiunea.
    29. Enunț: Când ieși din acest folder către cererea declarativă?
        Capcană: Când INSERT-ul are VALUES, pentru că spui ce vrei.
        Cheie: Când întrebarea este ce rând să citești și cine alege pașii. Celula rămâne aici, chiar dacă nu o ceri.
    30. Enunț: Când ieși către _2_SQL_Statements?
        Capcană: Niciodată: celula este toate propozițiile.
        Cheie: Când trebuie să spui dacă textul schimbă schema, rândurile, dreptul sau tranzacția. Aici întrebarea este ce rămâne stocat.

Eșec în producție:
    31. Enunț: Trimiți INSERT-ul într-o noapte în care entity_row a fost ștearsă. Ce găsești dimineața?
        Capcană: Tabela recreată și un rând cu 1.
        Cheie: Eroarea că tabela lipsește. Zero celule noi. INSERT nu recreează schema.
    32. Enunț: Un coleg spune că aplicația a afișat 1, deci stocul a crescut. Ce verifici?
        Capcană: Afișajul.
        Cheie: Dacă o propoziție a scris în entity_row și dacă rândul este acolo. SELECT 1 poate afișa 1 fără stoc.
    33. Enunț: Aliasul data_value a fost copiat într-un ALTER TABLE. Ce ai amestecat?
        Capcană: AS cere o coloană nouă.
        Cheie: AS numește rezultatul. Coloana stocată se schimbă printr-o propoziție de schemă, nu prin SELECT 1.
    34. Enunț: Cineva trimite comentariul „intersection of a row and a column”. Ce scrie serverul?
        Capcană: Înțelege și creează celula.
        Cheie: Nu este o propoziție. Nu scrie nimic.
    35. Enunț: Două sesiuni fac INSERT cu 1 în același timp. Câte celule ai voie să presupui fără să citești?
        Capcană: Una, pentru că valoarea este aceeași.
        Cheie: Fiecare INSERT terminat poate adăuga un rând. Fără citire nu știi numărul. În tur nu am rulat niciunul.
    36. Enunț: Grila rămâne deschisă și cineva o exportă ca tabelă. Ce s-a pierdut la închiderea clientului dacă textul era SELECT 1?
        Capcană: S-a pierdut și din entity_row.
        Cheie: S-a pierdut desenul. entity_row nu primise un rând din acel SELECT.
    37. Enunț: Pui USING INDEX în INSERT ca să grăbești celula. Ce ai schimbat la conținut?
        Capcană: Celula se scrie mai repede și indexul devine valoarea.
        Cheie: Conținutul tot este 1 în coloană, dacă propoziția rămâne validă. Indexul nu înlocuiește celula. Planul nu a fost măsurat.
    38. Enunț: Trimiți un șir gol în loc de INSERT. Ce record primește entity_row?
        Capcană: Un rând gol, care este tot o valoare de date.
        Cheie: Niciunul din acest șir. Șirul gol nu este propoziția. Numele metodei nu se trimite.
    39. Enunț: Aplicația construiește INSERT-ul și conexiunea primește Access denied. Ce s-a schimbat în entity_row?
        Capcană: Rândul a fost scris, apoi parola l-a ascuns.
        Cheie: Nimic din acest șir. Refuzul este înainte de propoziție.
    40. Enunț: Tratezi „colecție structurată” din manual ca pe un fișier pe care îl editezi cu editorul. Cine ține celula?
        Capcană: Fișierul de pe disc, deschis în editor.
        Cheie: Serverul, în tabelă, după o propoziție. Fișierul fizic din manual nu este drumul prin care scrii data value.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: 1 este doar pentru ecranul acestui proces. Ce alegi?
        Capcană: INSERT, ca să fie o valoare de date adevărată.
        Cheie: Rămâi în aplicație. Nu scrii un rând pe care nimeni nu îl citește.
    42. Enunț: Un al doilea client trebuie să citească același stoc peste o oră. Ce nu mai ajunge?
        Capcană: Îi trimiți lista Java.
        Cheie: Un INSERT terminat în tabelă. Lista din procesul tău moare odată cu procesul. SELECT 1 nu lasă stoc de citit mai târziu.
    43. Enunț: Întrebarea este „ce este un record”. Unde răspunzi?
        Capcană: Cu SELECT 1, pentru că întoarce un rând.
        Cheie: Aici: record-ul este rândul stocat. Rândul de rezultat nu este record-ul.
    44. Enunț: Întrebarea este „cine alege pașii de citire”. Unde răspunzi?
        Capcană: Cu INSERT, pentru că el creează datele.
        Cheie: La limbajul declarativ. Celula rămâne și fără să o ceri.
    45. Enunț: Trebuie să spui dacă textul schimbă schema sau rândurile. De ce nu ajunge acest folder?
        Capcană: Celula le acoperă pe toate.
        Cheie: Aici spui ce rămâne stocat. Clasificarea propozițiilor este în _2_SQL_Statements.
    46. Enunț: Cineva vrea o procedură care doar întoarce 1, ca să aibă o valoare de date. Când este prea mult?
        Capcană: Procedura este singura celulă posibilă.
        Cheie: Procedura nu este celula. Dacă vrei stoc, scrii un rând. Dacă vrei doar 1 pe ecran, nu scrii nimic.
    47. Enunț: JDBC este în pom. Este el celula?
        Capcană: Da, fără driver nu există tabelă.
        Cheie: Driverul cară propoziția. Celula este rândul scris de server. Un driver neconectat nu a scris.
    48. Enunț: Slide-ul de optimizer compară TRUNCATE cu DELETE. Îl folosești ca să explici data value?
        Capcană: Da, ambele șterg celule, deci sunt definiția.
        Cheie: Slide-ul este despre cine alege pașii între două propoziții. Nu este definiția celulei. Îl lași la folderul lor.
    49. Enunț: Îți trebuie cantitatea stocată a unui produs. De ce SELECT 1 AS data_value este unealta greșită?
        Capcană: Pui cantitatea în alias și raftul se actualizează.
        Cheie: Aliasul nu scrie celula. Cantitatea stocată este un rând într-o tabelă. Constanta lasă raftul necunoscut.
    50. Enunț: La interviu ți se cere efectul, propoziția și vecinul. Ce nu amesteci?
        Capcană: Închizi cu „o bază este o colecție” și treci la CREATE TABLE.
        Cheie: Efectul este o celulă în rând. Propoziția este INSERT INTO entity_row (data_value) VALUES (1). Vecinul este SELECT 1 AS data_value, care nu scrie celula.
*/
// Începător: banca ține întrebările despre celulă, fără un main care spune răspunsul.
// Producție: package _1_SQL._1_Introduction_To_DB.fast. Nu adăuga main de recall.
// Expert: Întrebările nu se lipesc pe WHERE sau pe JOIN. Vecinul lor este rândul de rezultat.
package _1_SQL._1_Introduction_To_DB.fast;

// Începător: clasa nu trimite INSERT-ul.
// Producție: public final class IntroductionToDb_Staff_Interview. Corpul rămâne fără main.
// Expert: Cele cincizeci stau în comentariu. Nu șterge din ele ca să rămână un alt număr.
public final class IntroductionToDb_Staff_Interview {
    // Începător: în corp nu există o a doua propoziție.
    // Producție: acolada închide IntroductionToDb_Staff_Interview.
    // Expert: storedDataValue rămâne dovada. Banca nu o înlocuiește.
}
