/*
TOPIC:
    Cincizeci de întrebări despre propoziția SELECT 1 AS data_value față de celula stocată și față de pașii scriși de tine.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI. EXPLAIN nu a fost citit.
INTENT:
    Recunoaștere din textul propoziției sau din eroare, momentul rezultatului, deosebirea de celulă, eșecul buclei și alegerea față de aplicație.
MECHANISM:
    Serverul execută SELECT 1 AS data_value doar dacă o primește. Rezultatul este la sfârșitul propoziției. Nicio tabelă nu este numită.
LOOKALIKES:
    Introduction_To_DB este celula. _2_SQL_Declarative_Language este faptul că optimizerul alege pașii. FOR EACH nu este niciuna.
BOOK THEORY:
    Manualul 9.7, 1.2.1, deschis în tur: baza este colecția structurată, SQL este limbajul de acces, serverul execută. PDF-ul spune că optimizerul alege abordarea la TRUNCATE față de DELETE și că numele Structured Query Language apare la o subcerere izolată. Nu conține SELECT 1 AS data_value.
BOOK EXAMPLE:
    SQL.statementTheServerExecutes și SQL.proseLoopTheServerRejects
REPO DEMO:
    src/main/java/_1_SQL/SQL.java
BREAK LAB:
    FOR EACH entity_row RETURN data_value
TRADE-OFFS:
    Aplicația când valoarea nu pleacă din proces. Tabela când îți trebuie celula. Folderul de propoziții când clasifici DDL sau DML.
ANTI-PATTERNS:
    Comentariul de curs sau bucla, trimise ca și cum ar fi propoziția.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească.
NOVEL TRANSFER:
    Cifra de control a unui colet, fără rând nou pe raft.
SPACED REVIEW:
    O zi: 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei propoziții SQL (SQL statement):
       Propoziția este textul pe care îl execută serverul. SELECT 1 AS data_value cere un rând de rezultat și nu scrie o celulă.

Exemplu:
       Sql_Terms.explainSqlStatement()
       Tabelele rămân neschimbate după ce rezultatul a plecat.

Definiția unei baze de date (database):
       Baza de date este colecția structurată. Serverul o prelucrează. Propoziția nu adaugă o colecție.

Exemplu:
       Sql_Terms.explainDatabase()
       SELECT 1 AS data_value nu creează o bază.

Definiția unei tabele (table):
       Tabela ține rândurile. Propoziția care nu o numește nu citește și nu scrie în ea.

Exemplu:
       Sql_Terms.explainTable()
       SELECT data_value FROM entity_row numește tabela. SELECT 1 AS data_value nu o numește.

Definiția unei coloane (column):
       Coloana este atributul vertical stocat. Aliasul din rezultat nu creează atributul în schemă.

Exemplu:
       Sql_Terms.explainColumn()
       data_value din SELECT 1 AS data_value este numele din rezultat.

Definiția unui rând (row):
       Rândul stocat este înregistrarea din tabelă. Rândul de rezultat trăiește până pleacă la client.

Exemplu:
       Sql_Terms.explainRow()
       Un 1 primit de client nu este un record din Introduction_To_DB.

🟢 Începător
    Ce este            cincizeci de întrebări despre propoziție, celulă și pașii scriși de tine
    De ce e nevoie     fără cheie scrisă, banca nu se poate corecta
🟡 Producție
    Cum se scrie       SELECT 1 AS data_value, iar eșecul este FOR EACH entity_row RETURN data_value
🔴 Staff
    Unde               rezultatul este la sfârșitul propoziției. Nu există main de recall.
Pași
    1. Recunoaște textul.
    2. Spune ce rămâne în tabelă.
    3. Desparte celula de propoziție.

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: În jurnal vezi SELECT 1 AS data_value și nicio tabelă în propoziție. Ce mecanism este?
       Capcană: Este un INSERT scurt, fiindcă a apărut o valoare.
       Cheie: Este propoziția care cere un rând de rezultat. Nu numește o tabelă, deci nu scrie o celulă. Efectul, dacă serverul o termină, este la sfârșitul propoziției.
    2. Enunț: Clientul a primit data_value egal cu 1, iar lista de tabele era goală și înainte. Ce recunoști?
       Capcană: Serverul a creat o tabelă temporară ca să aibă unde pune 1.
       Cheie: Rezultatul nu cere o tabelă. SELECT 1 AS data_value poate întoarce rândul fără un obiect nou în schemă. În tur, rândul nu a fost observat: root@localhost a fost refuzat.
    3. Enunț: EXPLAIN nu a fost rulat în tur. Din textul SELECT 1 AS data_value, ce index poți afirma că a fost ales?
       Capcană: Optimizerul alege întotdeauna PRIMARY, iar EXPLAIN ar fi arătat-o și fără să o citești.
       Cheie: Nu afirmi un plan pe care nu l-ai citit. Din text lipsește tabela, deci nu există o coloană stocată pe care un index să o caute.
    4. Enunț: Primești un refuz de sintaxă pe FOR EACH entity_row RETURN data_value. Ce nu a început?
       Capcană: Eroarea înseamnă că optimizerul a rulat bucla și a căzut la primul rând.
       Cheie: Textul nu este o propoziție. Serverul nu începe citirea. Nicio celulă nu se schimbă. În tur textul nu a fost trimis.
    5. Enunț: O metodă Java tipărește 1, iar jurnalul serverului nu are nicio propoziție. A rulat SQL?
       Capcană: Valoarea 1 este suficientă ca să spui că SQL a rulat.
       Cheie: Fără propoziție în jurnal, serverul nu a executat SQL. System.out nu este mecanismul. Mecanismul este șirul trimis.
    6. Enunț: Comentariul din SQL.java spune că o interogare face calculatorul să execute o operație. Care este propoziția?
       Capcană: Comentariul este propoziția, pentru că descrie efectul.
       Cheie: Comentariul nu este trimis la server. Propoziția este SELECT 1 AS data_value din statementTheServerExecutes.
    7. Enunț: Workbench desenează o grilă cu o celulă. Unde este mecanismul?
       Capcană: Click-ul care a deschis grila este mecanismul.
       Cheie: Mecanismul este propoziția pe care a executat-o serverul. Grila este desenul clientului. Click-ul nu este propoziția.
    8. Enunț: Vezi SELECT data_value FROM entity_row lângă SELECT 1 AS data_value. Care dintre ele nu numește o tabelă?
       Capcană: Ambele citesc entity_row, fiindcă data_value este același nume.
       Cheie: SELECT 1 AS data_value nu are FROM. SELECT data_value FROM entity_row numește tabela. Doar a doua cere un atribut stocat.
    9. Enunț: Jurnalul are textul propoziției, nu și rândul întors. Când vezi efectul?
       Capcană: Efectul este scris în jurnal în clipa în care propoziția este primită.
       Cheie: Pentru SELECT 1 AS data_value, efectul vizibil este rândul de rezultat la sfârșitul propoziției. Jurnalul care păstrează doar textul nu este rezultatul.
    10. Enunț: O eroare de autentificare Access denied pentru root@localhost apare înainte de orice propoziție. Ce s-a executat?
        Capcană: Serverul a rulat SELECT 1 și apoi a refuzat afișarea.
        Cheie: Refuzul este la conectare. statementTheServerExecutes nu a fost trimis. Efectul pe rânduri rămâne NERULAT AICI.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: După ce serverul termină SELECT 1 AS data_value, ce rând rămâne în vreo tabelă?
        Capcană: Rândul cu 1 rămâne până la COMMIT.
        Cheie: Nicio tabelă nu a fost numită, deci niciun rând stocat nu apare și nu dispare. Rândul de rezultat se vede la sfârșitul propoziției și pleacă la client.
    12. Enunț: Ce lacăt ține această propoziție după ce clientul a primit 1?
        Capcană: Ține un lacăt de rând pe data_value, ca să nu se schimbe 1.
        Cheie: Nu există rând stocat de ținut. Propoziția nu lasă un lacăt după ce rezultatul a plecat.
    13. Enunț: Ce index poate scurta SELECT 1 AS data_value?
        Capcană: Un index pe data_value, fiindcă aliasul este o coloană.
        Cheie: Nu există coloană stocată în propoziție. Un index nefolosit pe altă tabelă nu schimbă acest text.
    14. Enunț: O a doua sesiune întreabă tabelele după ce prima a primit 1. Ce vede din această propoziție?
        Capcană: Vede rândul, fiindcă rezultatul este în baza de date.
        Cheie: Rezultatul nu a fost scris într-o tabelă. A doua sesiune nu găsește un record produs de SELECT 1 AS data_value.
    15. Enunț: Propoziția stă într-o tranzacție deschisă, apoi dai ROLLBACK. Ce rând stocat dispare?
        Capcană: ROLLBACK șterge 1, deci valoarea nu a fost durabilă.
        Cheie: Nu a existat scriere. ROLLBACK nu are un rând din această propoziție pe care să îl anuleze. Rezultatul se văzuse deja la sfârșitul propoziției.
    16. Enunț: autocommit este 1. Schimbă el momentul în care vezi 1?
        Capcană: Trebuie să aștepți commit-ul implicit, altfel 1 nu este vizibil.
        Cheie: Nu este o scriere. Îl vezi la sfârșitul propoziției. autocommit nu transformă rezultatul într-o celulă.
    17. Enunț: Închizi conexiunea după ce clientul a tipărit 1. Ce se rostogolește înapoi?
        Capcană: Închiderea șterge valoarea din tabelă.
        Cheie: Nu era în tabelă. Închiderea nu este un ROLLBACK al unui record produs de SELECT 1.
    18. Enunț: Când se vede efectul: la primirea textului, la commit sau la sfârșitul propoziției?
        Capcană: La commit, ca orice schimbare.
        Cheie: La sfârșitul propoziției, ca rând de rezultat. Commit-ul nu este momentul, pentru că schema și rândurile stocate nu se schimbă.
    19. Enunț: Poate un lacăt de tabelă să fie consecința lui SELECT 1 AS data_value?
        Capcană: Orice SELECT blochează toate tabelele până la deconectare.
        Cheie: Propoziția nu numește o tabelă. Nu ai, din acest text, un lacăt de tabelă de arătat.
    20. Enunț: rows_examined nu a fost citit în tur. Ce poți spune totuși despre rândurile stocate atinse de text?
        Capcană: Optimizerul examinează toate tabelele din instanță ca să producă 1.
        Cheie: Textul nu numește o tabelă, deci nu cere un rând stocat. Cifra din plan rămâne necitită, pentru că autentificarea a căzut.

Deosebire de mecanismul vecin:
    21. Enunț: De ce SELECT 1 AS data_value nu este o celulă din Introduction_To_DB?
        Capcană: data_value este definit acolo ca valoarea din celulă, deci propoziția scrie acea celulă.
        Cheie: Celula este conținutul de la întâlnirea unui rând cu o coloană și există fără propoziție. Această propoziție nu numește rândul și nu lasă celula.
    22. Enunț: De ce nu scrii tu pașii, dacă PDF-ul spune că optimizerul are abordări?
        Capcană: Abordarea se scrie în propoziție, altfel optimizerul nu știe ce vrei.
        Cheie: Tu numești rezultatul. Optimizerul alege abordarea. FOR EACH entity_row RETURN data_value îi dictează pașii și nu este propoziția.
    23. Enunț: De ce CREATE TABLE entity_row (data_value INT) nu este exemplul acestui folder?
        Capcană: Fără tabelă nu există SQL, deci CREATE este primul pas obligatoriu.
        Cheie: CREATE schimbă schema. Aici întrebarea este propoziția care cere un rezultat fără să lase o celulă. Clasificarea DDL stă în _2_SQL_Statements.
    24. Enunț: De ce INSERT INTO entity_row VALUES (1) nu înlocuiește SELECT 1 AS data_value?
        Capcană: Ambele pun 1 undeva, deci lasă aceeași stare.
        Cheie: INSERT lasă un record. SELECT 1 lasă un rând de rezultat și tabela rămâne cum era. După INSERT, celula există și fără să mai întrebi.
    25. Enunț: O coloană descrisă în comentariul Introduction_To_DB este deja un data_value?
        Capcană: Da, descrierea este valoarea.
        Cheie: Coloana este atributul. Data value este conținutul celulei. Comentariul nu pune conținutul și nu este o propoziție.
    26. Enunț: O listă Java cu un singur 1 este un rezultat de la server?
        Capcană: Da, dacă o numești data_value.
        Cheie: Lista nu a trecut prin server. Rezultatul de la server apare doar după propoziție. Manualul spune că poți ascunde SQL-ul după un API, dar atunci mecanismul tot este propoziția din spate, nu lista.
    27. Enunț: De ce un click în Workbench nu dovedește mecanismul?
        Capcană: Grila arată că SQL a rulat, indiferent de text.
        Cheie: Dovada este propoziția executată de server. Click-ul este clientul. Fără textul trimis, nu știi dacă ai cerut un rezultat sau ai deschis o tabelă.
    28. Enunț: Șirul stă în Java și nu este trimis. S-a executat propoziția?
        Capcană: Șirul compilat este deja o execuție.
        Cheie: Compilarea păstrează textul. Serverul execută doar ce primește. În tur nu a primit: Access denied.
    29. Enunț: Când ieși din acest folder către o propoziție care citește o celulă?
        Capcană: SELECT 1 AS data_value citește deja celula, dacă numele coincid.
        Cheie: Când rezultatul trebuie să fie conținutul stocat. Atunci propoziția numește tabela, ca SELECT data_value FROM entity_row, și nu mai ești la exemplul fără tabelă.
    30. Enunț: Ce întrebare rămâne la _2_SQL_Declarative_Language și ce rămâne aici?
        Capcană: Declarativ înseamnă orice comentariu care spune „vreau datele”.
        Cheie: Aici alegi propoziția față de celulă. La declarativ, întrebarea este cine alege pașii: tu nu îi scrii, optimizerul îi alege. Comentariul singur nu este niciuna.

Eșec în producție:
    31. Enunț: Trimiți FOR EACH entity_row RETURN data_value într-o noapte de încărcare. Ce vezi dimineața în tabele?
        Capcană: Jumătate din rânduri au fost parcurse și întoarse.
        Cheie: Serverul nu începe textul. Tabelele rămân cum erau înainte de trimitere. Nu există un rezultat parțial din această buclă.
    32. Enunț: Un coleg spune că SELECT 1 AS data_value a inserat un rând, pentru că aplicația a afișat 1. Ce verifici?
        Capcană: Afișajul este dovada scrierii.
        Cheie: Verifici dacă o tabelă a fost numită și dacă un rând nou stă în ea. Această propoziție nu numește o tabelă. Afișajul poate veni și din memorie.
    33. Enunț: Aliasul data_value a fost copiat într-un ALTER TABLE generat de aplicație. Ce ai amestecat?
        Capcană: AS cere serverului să adauge coloana.
        Cheie: AS numește coloana din rezultat. Coloana stocată se schimbă printr-o propoziție de schemă, care nu este acest SELECT.
    34. Enunț: Cineva lipește în client comentariul „piece of code inducing the computer”. Ce execută serverul?
        Capcană: Înțelege intenția și întoarce rezultatul dorit.
        Cheie: Nu este o propoziție. Nu pornește. Comentariul rămâne text de curs.
    35. Enunț: API-ul ascunde SQL-ul și în Java scrii o buclă peste o listă. Ce a văzut optimizerul?
        Capcană: A văzut bucla, fiindcă manualul spune că API-ul poate ascunde sintaxa.
        Cheie: Optimizerul vede propoziția care chiar a fost trimisă, nu bucla din aplicație. Dacă nu a plecat nicio propoziție, optimizerul nu a văzut nimic.
    36. Enunț: Grila rămâne deschisă în Workbench și cineva o ia drept tabelă persistentă. Ce se pierde la închiderea clientului?
        Capcană: Se pierde și din server, deci SELECT 1 scrisese o tabelă.
        Cheie: Se pierde desenul clientului. Serverul nu avea un record de la SELECT 1 AS data_value pe care să îl piardă.
    37. Enunț: Adaugi în propoziție cuvintele USING INDEX ca să obligi planul lui SELECT 1. Ce ai schimbat?
        Capcană: Ai ajutat optimizerul, iar rezultatul rămâne 1, doar că mai repede.
        Cheie: Nu există coloană stocată al cărei index să îl obligi. Textul care dictează pașii iese din propoziția declarativă. Planul nu a fost măsurat în tur.
    38. Enunț: Trimiți un șir gol în loc de SELECT 1 AS data_value. Ce rând primește clientul?
        Capcană: Serverul presupune 1, fiindcă metoda Java are acest nume.
        Cheie: Șirul gol nu este propoziția. Clientul nu primește rândul de rezultat al lui SELECT 1. Numele metodei nu este trimis.
    39. Enunț: Aplicația construiește șirul, dar conexiunea a primit Access denied. Ce s-a schimbat în tabele?
        Capcană: Propoziția a rulat pe server până la refuzul parolei.
        Cheie: Refuzul este înainte de propoziție. Tabelele nu au fost atinse de acest șir. Efectul rămâne NERULAT AICI.
    40. Enunț: Tratezi fraza „SQL standard” din manual ca pe un al doilea motor care execută SELECT 1. Cine execută?
        Capcană: Standardul execută, iar MySQL doar afișează.
        Cheie: Execută serverul MySQL care a primit propoziția. Standardul este limbajul. Fără server care acceptă conexiunea, nu ai un rezultat de arătat.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: Valoarea 1 este doar pentru ecranul acestui proces. Ce alegi?
        Capcană: Trimiți totuși SELECT 1, ca să fie „în SQL”.
        Cheie: Rămâi în aplicație. Serverul nu trebuie să vadă valoarea. Propoziția este pentru un rezultat care vine de la server.
    42. Enunț: Un al doilea client trebuie să primească aceeași valoare de la același server. Ce nu mai ajunge?
        Capcană: Trimiți lista Java prin rețeaua aplicației și o numești SQL.
        Cheie: Trimiți propoziția la server. Lista din procesul tău nu este rezultatul celuilalt client. SELECT 1 AS data_value nu scrie totuși o celulă pe care al doilea să o citească mai târziu dintr-o tabelă.
    43. Enunț: Întrebarea de interviu este „ce este un record”. Unde răspunzi?
        Capcană: Cu SELECT 1 AS data_value, fiindcă întoarce un rând.
        Cheie: La Introduction_To_DB: record-ul este rândul stocat. Rândul de rezultat nu este record-ul.
    44. Enunț: Întrebarea este „cine împarte lucrul în pași”. Unde răspunzi?
        Capcană: Tu, în FOR EACH, fiindcă altfel serverul nu știe ordinea.
        Cheie: La limbajul declarativ: tu numești rezultatul, optimizerul alege pașii. PDF-ul arată asta pe TRUNCATE față de DELETE, nu pe bucla ta.
    45. Enunț: Trebuie să spui dacă o propoziție schimbă schema sau rândurile. Ce folder nu mai este suficient?
        Capcană: _1_SQL ajunge, fiindcă SQL le face pe toate.
        Cheie: Acest folder alege între celulă și propoziție. Clasificarea DDL, DML, DCL și TCL este în _2_SQL_Statements. Nu o preda dintr-un singur SELECT 1.
    46. Enunț: Cineva vrea o procedură stocată ca să întoarcă 1. Când este prea mult?
        Capcană: Procedura este singura cale prin care serverul întoarce o valoare.
        Cheie: SELECT 1 AS data_value este de ajuns când nu ai decât rezultatul. Procedura este alt mecanism: se apelează cu CALL și nu este celula din tabelă.
    47. Enunț: JDBC este în pom, la Connector/J 26.10.0. Este el mecanismul?
        Capcană: Da, pentru că fără driver propoziția nu există.
        Cheie: Driverul cară propoziția. Mecanismul este textul executat de server. Un driver care nu s-a conectat, ca în tur, nu a executat nimic.
    48. Enunț: Slide-ul de optimizer compară TRUNCATE cu DELETE. Îl folosești ca să explici SELECT 1?
        Capcană: Da, orice diferență de optimizer este acest folder.
        Cheie: Slide-ul desparte două propoziții care golesc sau șterg. Nu este propoziția fără tabelă. Îl lași la folderul acelor propoziții.
    49. Enunț: Îți trebuie cantitatea stocată a unui produs. De ce SELECT 1 AS data_value este unealta greșită?
        Capcană: Pui cantitatea în alias și tabela se actualizează.
        Cheie: Aliasul nu scrie celula. Ca să citești cantitatea stocată, propoziția trebuie să numească tabela. Altfel clientul primește o constantă, iar raftul rămâne necunoscut.
    50. Enunț: La interviu ți se cere într-un minut efectul, propoziția și vecinul. Ce nu ai voie să amesteci?
        Capcană: Închizi cu „SQL este un limbaj” și treci la CREATE TABLE.
        Cheie: Efectul este un rând de rezultat, nu o celulă. Propoziția este SELECT 1 AS data_value. Vecinul este celula din Introduction_To_DB, care există fără această propoziție. Pașii scriși de tine eșuează ca FOR EACH.
*/
// Începător: banca ține cincizeci de întrebări despre propoziție, nu un main care să spună răspunsul.
// Producție: package _1_SQL.fast. Nu adăuga main de recall.
// Expert: Întrebările nu se lipesc pe WHERE, pe JOIN sau pe TRUNCATE. Vecinul lor este celula stocată.
package _1_SQL.fast;

// Începător: clasa nu trimite propoziția și nu citește cheia la cerere.
// Producție: public final class Sql_Staff_Interview. Corpul rămâne fără main.
// Expert: Cele cincizeci stau în comentariu, cu cheia scrisă. Nu șterge din ele ca să rămână un alt număr.
public final class Sql_Staff_Interview {
    // Începător: nu există o a doua propoziție în corp.
    // Producție: acolada închide Sql_Staff_Interview.
    // Expert: statementTheServerExecutes rămâne dovada din SQL.java. Banca nu o înlocuiește.
}
