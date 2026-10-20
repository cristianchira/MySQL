// Începător: pachetul ține banca de interviu despre verificarea privilegiilor.
// Producție: package _13_MySQL_Security.fast. Fără linie, cele 50 de întrebări nu au folder.
// Expert: pachetul nu are main și nu arată cheia într-o rulare de recall. Nu scrie în mysql.user.
package _13_MySQL_Security.fast;

// Începător: banca întoarce propozițiile din clasa de topic.
// Producție: import _13_MySQL_Security.MySQL_Security. Fără linie, metodele nu pot preda SHOW GRANTS.
// Expert: importul nu copiază SELECT CONNECTION_ID(), USER(), CURRENT_USER() din folderul de client.
import _13_MySQL_Security.MySQL_Security;

/*
TOPIC:
    Banca întreabă cum citești privilegiile unui user@host și când scrierea directă nu ajunge la etapa 2.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    După SHOW GRANTS rămâne o listă, nu o tabelă de aplicație modificată. Un rol din listă nu este activ.
MECHANISM:
    mysqld verifică fiecare cerere pe copia din memorie a tabelelor de grant. GRANT reîncarcă memoria. UPDATE pe mysql.user nu o face. FLUSH PRIVILEGES o face, este depreciat și dă commit implicit.
LOOKALIKES:
    GRANT. Se separă prin cine scrie: GRANT scrie privilegiul și reîncarcă; SHOW GRANTS citește user și host.
BOOK THEORY:
    Manualul 9.7: 'joe'@'office.example.com' și 'joe'@'home.example.com' au liste separate. Un privilegiu acordat este în vigoare; un rol nu, până la SET ROLE sau rolul implicit. partial_revokes oprit dă ERROR 1141 la REVOKE pe schemă după un grant global; pornit, SHOW GRANTS arată linia REVOKE. După reîncărcare, privilegiul de tabelă intră la cererea următoare, cel de bază la USE, cel global static la o conexiune nouă, cel dinamic imediat. FLUSH este commit implicit. FLUSH PRIVILEGES este depreciat și nu este necesar după GRANT. Cu --skip-grant-tables nu există verificare până la flush. Nu există PDF de curs în folder.
BOOK EXAMPLE:
    MySQL_Security.showGrantsAtOffice()
REPO DEMO:
    src/main/java/_13_MySQL_Security/MySQL_Security.java
BREAK LAB:
    UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; lasă etapa 2 pe copia veche. După reîncărcare, sesiunea deja deschisă tot nu primește privilegiul global static.
TRADE-OFFS:
    SHOW GRANTS ca să citești. GRANT ca să scrii. partial revoke ca să tai o schemă dintr-un global. Aplicația când regula este pe un rând.
ANTI-PATTERNS:
    UPDATE direct pe mysql.user, sau un privilegiu global în tabela user pentru un cont care avea nevoie doar de o tabelă.
STAFF QUESTIONS:
    recognizeShowGrants(), effectOnGrantMemory(), separateFromGrant(), failDirectUpdate(), chooseGrantOrApplication()
NOVEL TRANSFER:
    Clinică: casierul de la recepție și același nume de acasă. Payroll se citește doar de la recepție. Rândul unui pacient rămâne în aplicație.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile: lista, memoria, GRANT, rândul care nu se filtrează aici.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE citești joe de la birou -> recognizeShowGrants() -> SHOW GRANTS FOR 'joe'@'office.example.com'; -> nu activează un rol
    2. Eșecul cel mai mic
           -> DECIDE nu scrii direct în mysql.user -> failDirectUpdate() -> UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; -> etapa 2 rămâne pe copia veche
    3. Granița
           -> propoziția este a lui mysqld; banca aceasta nu este mecanismul
    4. Alternativa
           -> separateFromGrant() când compari cu GRANT; chooseGrantOrApplication() când regula este pe un rând
    5. Anti-patternul
           -> UPDATE pe mysql.user nu publică Select_priv în sesiunea deschisă
    6. Dovada
           -> MySQL_Security.showGrantsAtOffice(), showGrantsAtHome(), directGrantTableUpdate(), reloadGrantTables()
    7. Interviu
           -> SHOW GRANTS citește user@host; GRANT scrie și reîncarcă; FLUSH PRIVILEGES nu acordă; rândul nu se taie cu privilegiul de tabelă
*/

/*
Definiția unei verificări de cerere (request verification):
       După conexiune, serverul verifică fiecare propoziție pe copia din memorie a privilegiilor.

Exemplu:
       MySQLSecurity_Terms.explainRequestVerification()
       Fără privilegiul din copie, rândurile aplicației rămân neatinse.

Definiția unui cont MySQL (account):
       Contul este numele împreună cu hostul. Același nume la alt host este alt cont.

Exemplu:
       MySQLSecurity_Terms.explainAccount()
       joe de la birou nu împrumută lista lui joe de acasă.

Definiția unor tabele de grant (grant tables):
       Privilegiile stau în schema mysql. Serverul decide pe copia lor din memorie.

Exemplu:
       MySQLSecurity_Terms.explainGrantTables()
       Un UPDATE direct nu schimbă copia până la reîncărcare sau repornire.

Definiția unei listări de privilegii (SHOW GRANTS):
       SHOW GRANTS citește privilegiile scrise pentru contul numit și nu le acordă.

Exemplu:
       MySQLSecurity_Terms.explainShowGrants()
       Un rol din listă nu este încă activ.

Definiția unei reîncărcări de privilegii (FLUSH PRIVILEGES):
       FLUSH PRIVILEGES recitește tabelele de grant. În 9.7 este depreciat și face commit implicit.

Exemplu:
       MySQLSecurity_Terms.explainFlushPrivileges()
       Nu este necesar după GRANT.
*/

/*
Recunoaștere din SQL, din lista SHOW GRANTS sau din eroare. 10.
1. Enunț: Vezi SHOW GRANTS FOR 'joe'@'office.example.com'; . Ce cont citești?
   Capcană: „Citești privilegiile oricărui joe, indiferent de host.”
   Cheie: Contul este joe de la office.example.com. joe de la home.example.com este alt rând și poate avea alt set.
   Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
2. Enunț: În SHOW GRANTS apare REVOKE INSERT ON `world`.* lângă GRANT INSERT ON *.*. INSERT global a dispărut?
   Capcană: „Linia REVOKE a șters privilegiul global de peste tot.”
   Cheie: Cu partial_revokes, linia taie INSERT doar pe schema world. Pe celelalte scheme INSERT global rămâne.
   Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
3. Enunț: ERROR 1141 There is no such grant defined for user 'u1' on host '%' după REVOKE pe o schemă. Ce este oprit?
   Capcană: „Contul nu există, deci îl repari cu CREATE USER.”
   Cheie: partial_revokes este oprit implicit. REVOKE pe schemă nu taie un privilegiu global până nu pornești variabila. SHOW GRANTS nu capătă linia REVOKE.
   Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
4. Enunț: SHOW GRANTS listează un rol. Etapa 2 îl folosește la propoziția următoare?
   Capcană: „Orice nume din SHOW GRANTS este deja activ.”
   Cheie: Un privilegiu acordat contului este în vigoare. Un rol nu este, până la rolul implicit sau SET ROLE în sesiune.
   Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
5. Enunț: Vezi UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Ce a schimbat etapa 2?
   Capcană: „Coloana s-a scris, deci următorul SELECT al lui jeffrey trece.”
   Cheie: Rândul de pe disc se poate schimba. Verificarea folosește copia din memorie, rămasă veche până la reîncărcare sau repornire.
   Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
6. Enunț: Cineva pune FLUSH PRIVILEGES imediat după GRANT SELECT pe o tabelă. Care propoziție a publicat privilegiul?
   Capcană: „GRANT nu se aplică până nu faci FLUSH PRIVILEGES.”
   Cheie: GRANT reîncarcă tabelele de grant imediat. FLUSH PRIVILEGES este pentru scrierea directă, și în 9.7 este depreciat.
   Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
7. Enunț: A doua propoziție este SHOW GRANTS FOR 'joe'@'home.example.com'; . De ce nu este un duplicat?
   Capcană: „Hostul este doar afișare, privilegiile sunt ale numelui.”
   Cheie: Identitatea este nume și host. Serverul poate ține privilegii diferite pentru cele două conturi.
   Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
8. Enunț: Serverul a pornit cu --skip-grant-tables. Ce propoziție reaprinde verificarea?
   Capcană: „Orice GRANT, pentru că fără el nimeni nu are drepturi.”
   Cheie: Cu opțiunea, serverul nu citește tabelele de grant și nu face controlul accesului. FLUSH PRIVILEGES le citește și pornește verificarea.
   Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
9. Enunț: SHOW GRANTS FOR 'bob'@'pc84.example.com'; a întors lista. S-a scris un rând în tabela de salarii?
   Capcană: „SHOW schimbă granturile, deci și datele legate de cont.”
   Cheie: La sfârșitul propoziției ai lista. Manualul o descrie ca citire a privilegiilor, nu ca scriere în tabele de aplicație.
   Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
10. Enunț: REVOKE a răspuns Query OK, 0 rows affected. Unde vezi ce a rămas?
    Capcană: „0 rows affected înseamnă că REVOKE nu a făcut nimic, deci nu mai citești nimic.”
    Cheie: Manualul spune să citești SHOW GRANTS ca să vezi privilegiile rămase. Numărul de rânduri afectate nu este lista.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()

Rânduri, memorie, privilegiu și momentul în care se vede efectul. 10.
11. Enunț: După UPDATE pe mysql.user, când vede etapa 2 valoarea nouă?
    Capcană: „La commit-ul tranzacției care a făcut UPDATE.”
    Cheie: Până la FLUSH PRIVILEGES sau repornire, verificarea folosește copia veche. Commit-ul rândului nu este reîncărcarea.
    Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
12. Enunț: După reîncărcare, un privilegiu de tabelă când intră într-o sesiune deja deschisă?
    Capcană: „Doar după ce clientul se reconectează.”
    Cheie: Privilegiul de tabelă sau de coloană se aplică la cererea următoare a clientului.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
13. Enunț: După reîncărcare, un privilegiu de bază de date când se aplică?
    Capcană: „La aceeași cerere următoare, ca privilegiul de tabelă.”
    Cheie: La următorul USE al acelei baze. Un client care ține numele bazei în cache poate să nu vadă schimbarea până nu schimbă baza.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
14. Enunț: Select_priv a fost pus pe 'Y', apoi ai reîncărcat. Sesiunea deja deschisă primește SELECT global?
    Capcană: „FLUSH PRIVILEGES împinge orice privilegiu în toate sesiunile deschise.”
    Cheie: Privilegiile globale statice și parolele nu se schimbă pentru clientul deja conectat. Se văd la conexiunile următoare. Privilegiile globale dinamice se aplică imediat.
    Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
15. Enunț: SHOW GRANTS pentru contul curent arată un rol, iar SET ROLE nu a rulat. Care drept din listă intră la cererea următoare?
    Capcană: „Rolul din listă se adună la privilegii de la sine.”
    Cheie: Privilegiile acordate direct contului intră. Rolul așteaptă activarea. Schimbarea rolurilor active se vede imediat, doar în sesiunea aceea, fără FLUSH PRIVILEGES.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
16. Enunț: FLUSH PRIVILEGES găsește o tranzacție deschisă pe tabele InnoDB de salarii. Ce se întâmplă cu ea?
    Capcană: „FLUSH atinge doar memoria de privilegii și lasă tranzacția deschisă.”
    Cheie: FLUSH este propoziție administrativă și face commit implicit, ca și cum ai fi dat COMMIT înainte.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
17. Enunț: O tranzacție de salarii este deschisă și rulezi SHOW GRANTS. Tranzacția se închide?
    Capcană: „Orice propoziție despre privilegii închide tranzacția.”
    Cheie: GRANT este pe lista de commit implicit pentru că modifică tabele din mysql. SHOW GRANTS nu este pe listă. Rândurile de salarii rămân în tranzacția deschisă.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
18. Enunț: Unde stau privilegiile pe disc și unde decide etapa 2?
    Capcană: „Etapa 2 citește de fiecare dată rândul din mysql.user de pe disc.”
    Cheie: Pe disc stau tabelele de grant din schema mysql. La pornire serverul le citește în memorie și decide pe copie.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
19. Enunț: Tabela user are DELETE global pe 'Y'. Mai caută serverul în db înainte să lase ștergerea?
    Capcană: „Adună mereu global, bază, tabelă și coloană.”
    Cheie: Dacă privilegiul global permite operația, accesul este acordat, în afară de un partial revoke. Pentru un privilegiu doar administrativ, nici nu caută în db.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
20. Enunț: SHOW GRANTS a întors lista. Indexul tabelei de salarii s-a schimbat?
    Capcană: „Citirea de privilegii reconstruiește indexul tabelei pe care o protejează.”
    Cheie: Nu. Propoziția nu atinge indexul unei tabele de aplicație. Rezultatul este lista de privilegii.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()

Deosebire de GRANT. 10.
21. Enunț: De ce SHOW GRANTS nu înlocuiește GRANT SELECT pe payroll.salary pentru joe de la birou?
    Capcană: „Dacă SHOW GRANTS nu arată SELECT, îl repeți până apare.”
    Cheie: SHOW GRANTS citește ce este scris pentru cont. GRANT scrie privilegiul și reîncarcă memoria imediat.
    Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
22. Enunț: CREATE USER a reușit și joe s-a conectat. De ce SELECT pe salary tot poate fi refuzat?
    Capcană: „Un cont autentificat poate citi tabelele din instanță.”
    Cheie: CREATE USER ține de etapa 1. Etapa 2 refuză propoziția dacă privilegiul nu este în copia din memorie. GRANT este cel care îl scrie. SHOW GRANTS arată lipsa.
    Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
23. Enunț: GRANT ALL ON db1.* stă în folderul GRANT. De ce nu este propoziția acestui folder?
    Capcană: „Securitatea și GRANT sunt aceeași propoziție, deci o ții aici.”
    Cheie: GRANT scrie privilegiul. Aici propoziția este SHOW GRANTS, care arată ce a rămas scris pentru un host anume.
    Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
24. Enunț: Aceeași persoană folosește două calculatoare. Un singur SHOW GRANTS ajunge?
    Capcană: „Numele de utilizator este identitatea, hostul este decor.”
    Cheie: joe@office și joe@home sunt conturi diferite. Fiecare are propriul SHOW GRANTS.
    Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
25. Enunț: Conexiunea este criptată. Mai verifică etapa 2 privilegiul?
    Capcană: „TLS înseamnă că SELECT este permis.”
    Cheie: Capitolul 8.3 protejează octeții pe rețea. Etapa 2 tot verifică copia din memorie. SHOW GRANTS nu se schimbă fiindcă ai TLS.
    Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
26. Enunț: REVOKE a rulat, iar SHOW GRANTS încă listează privilegiul. REVOKE nu a mers?
    Capcană: „SHOW GRANTS reaplică REVOKE, deci lista veche înseamnă că REVOKE a eșuat.”
    Cheie: SHOW GRANTS citește ce a rămas. 0 rows affected nu este lista. O linie REVOKE apare în listă doar pentru un partial revoke reușit, nu pentru orice REVOKE.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
27. Enunț: Un privilegiu în tabela user și unul în tables_priv așteaptă la fel după reîncărcare?
    Capcană: „Toate privilegiile sunt globale, iar celelalte tabele sunt doar istoric.”
    Cheie: user și global_grants sunt globale. Un privilegiu global static așteaptă o conexiune nouă. tables_priv, după reîncărcare, intră la cererea următoare.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
28. Enunț: mysqladmin flush-privileges și FLUSH PRIVILEGES. Ce trimite un driver?
    Capcană: „Sunt același text, deci driverul poate trimite numele comenzii mysqladmin.”
    Cheie: FLUSH PRIVILEGES este propoziția. mysqladmin flush-privileges este un program, nu un String de JDBC.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
29. Enunț: Poți pune o parolă doar pe schema payroll, alta pe mysql, pentru același cont?
    Capcană: „IDENTIFIED BY se leagă de tabelă, ca un privilegiu de coloană.”
    Cheie: Parola este a contului, global. SHOW GRANTS nu o arată și nu o leagă de o tabelă. Nu da citire pe schema mysql unui cont care nu administrează.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
30. Enunț: SHOW GRANTS arată un rol acordat fără clauza ON. Este un privilegiu de tabelă?
    Capcană: „Fără ON, GRANT a dat SELECT pe toate tabelele.”
    Cheie: Fără ON, GRANT acordă roluri, nu privilegii. Rolul din SHOW GRANTS nu intră în etapa 2 până nu este activ.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()

Eșec în producție. 10.
31. Enunț: UPDATE pe mysql.user, apoi SELECT pe salary în aceeași sesiune, fără reîncărcare. De ce SELECT tot eșuează?
    Capcană: „Rândul scris în mysql.user este deja dreptul.”
    Cheie: Etapa 2 a rămas pe copia veche. SELECT nu vede Select_priv nou.
    Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
32. Enunț: Ai făcut FLUSH PRIVILEGES după Select_priv = 'Y', iar sesiunea deschisă tot nu are SELECT global. De ce?
    Capcană: „Reîncărcarea aplică privilegiul global static pe loc, în toate sesiunile.”
    Cheie: Privilegiul global static intră la conexiunile următoare. Sesiunea deja legată rămâne cu vechiul set.
    Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
33. Enunț: SHOW GRANTS nu conține o linie REVOKE pe world, deși ai încercat să tai INSERT global. Ce a rămas în vigoare?
    Capcană: „Lipsește linia, deci INSERT pe world a fost tăiat în tăcere.”
    Cheie: Fără partial_revokes, REVOKE pe schemă eșuează cu ERROR 1141. Privilegiul global rămâne, iar SHOW GRANTS nu capătă linia REVOKE.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
34. Enunț: FLUSH PRIVILEGES pe un server care nu a pornit cu --skip-grant-tables și în care nimeni nu a scris direct în tabelele de grant. Ce privilegiu nou apare?
    Capcană: „Flush-ul acordă drepturile care lipseau.”
    Cheie: Niciunul. Reîncarcă ce era deja. Nu acordă. În 9.7 dă și avertisment de depreciere.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
35. Enunț: UPDATE a pus Select_priv = 'Y' în mysql.user pentru un cont de aplicație, iar reîncărcarea a ajuns într-o conexiune nouă. Ce poate citi?
    Capcană: „Select_priv din user înseamnă doar schema mysql, nu payroll.”
    Cheie: Select_priv global permite citirea din orice tabelă, înainte de tables_priv. Manualul lasă privilegiile din user pe 'N' pentru cine nu administrează. Scrierea directă tot nu este calea.
    Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
36. Enunț: Pe 9.7 scrii direct în mysql.user ca să repari un cont. Ce spune manualul despre cale?
    Capcană: „Este calea normală, iar FLUSH PRIVILEGES o face sigură.”
    Cheie: Scrierea directă nu este recomandată și este tratată ca depreciată. Calea este CREATE USER, GRANT, REVOKE. FLUSH PRIVILEGES însuși este depreciat.
    Demonstrație: MySQLSecurity_Staff_Interview.failDirectUpdate()
37. Enunț: De ce nu pui FLUSH PRIVILEGES după fiecare GRANT, chiar dacă golește un cache?
    Capcană: „Fără flush, cache-ul caching_sha2_password ține privilegiul vechi.”
    Cheie: GRANT deja și-a reîncărcat privilegiile. Flush-ul golește cache-ul de autentificare și memoria rămasă după GRANT sau CREATE USER, dar nu este cel care publică privilegiul, iar propoziția este depreciată.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
38. Enunț: Ai reîncărcat un privilegiu de bază, iar următorul SELECT în aceeași bază nu îl vede. Ce a rămas în urmă?
    Capcană: „Reîncărcarea a eșuat, pentru că SELECT nu a așteptat.”
    Cheie: Privilegiul de bază intră la următorul USE. Cache-ul de nume din client poate ascunde efectul până schimbi baza.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
39. Enunț: Serverul a pornit cu --skip-grant-tables ca să repari un cont. Ce poate face orice conexiune până la FLUSH PRIVILEGES?
    Capcană: „Doar root local poate intra, ceilalți sunt respinși.”
    Cheie: Oricine se poate conecta și poate face orice operație. Nu există controlul accesului până la FLUSH PRIVILEGES.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
40. Enunț: Propoziția din repo numește 'joe'@'office.example.com', nu lasă serverul să aleagă contul sesiunii. De ce?
    Capcană: „SHOW GRANTS fără cont este aceeași listă pentru toate hosturile.”
    Cheie: Propoziția numește user și host ca să citești contul de la birou, nu contul cu care s-a întâmplat să fie autentificată sesiunea care o trimite.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()

Compromis, inclusiv când rămâi în aplicație. 10.
41. Enunț: Când rămâi la SHOW GRANTS și când treci la GRANT?
    Capcană: „Le schimbi mereu, pentru că spun același lucru.”
    Cheie: SHOW GRANTS când vrei lista pentru un user@host. GRANT când lista nu conține privilegiul și trebuie să îl scrii.
    Demonstrație: MySQLSecurity_Staff_Interview.chooseGrantOrApplication()
42. Enunț: Regula „un casier vede doar rândurile lui” este un privilegiu MySQL?
    Capcană: „Un privilegiu de tabelă ascunde rândurile celorlalți.”
    Cheie: Privilegiile sunt globale, pe schemă, tabelă, coloană sau rutină. Nu poți refuza explicit un utilizator și nu filtrezi un rând. Rândul rămâne în aplicație sau într-un view.
    Demonstrație: MySQLSecurity_Staff_Interview.chooseGrantOrApplication()
43. Enunț: Când folosești un partial revoke în loc să dai privilegiul doar pe o schemă?
    Capcană: „Îl folosești mereu, pentru că este mai scurt decât GRANT pe o schemă.”
    Cheie: Când contul are deja privilegiu global și trebuie să piardă o schemă, cu partial_revokes pornit. Altfel acorzi la nivel de schemă și lași user pe 'N'. Nu folosești UPDATE pe mysql.user.
    Demonstrație: MySQLSecurity_Staff_Interview.chooseGrantOrApplication()
44. Enunț: Contul este blocat sau parola a expirat. SHOW GRANTS îl repară?
    Capcană: „Lista de privilegii deblochează contul.”
    Cheie: Blocarea se citește la etapa 1, în account_locked, înainte de etapa 2. SHOW GRANTS nu deblochează. CREATE USER sau ALTER USER schimbă starea, nu această propoziție.
    Demonstrație: MySQLSecurity_Staff_Interview.chooseGrantOrApplication()
45. Enunț: Când lași verificarea în aplicație, nu pe server?
    Capcană: „Orice refuz de acces se scrie ca privilegiu, ca să nu stea în cod.”
    Cheie: Rămâi pe server când întrebarea este „poate rula SELECT pe tabela salary”. Rămâi în aplicație când nu poți exprima regula: un rând, un refuz explicit, sau CREATE pe tabele fără CREATE pe bază.
    Demonstrație: MySQLSecurity_Staff_Interview.chooseGrantOrApplication()
46. Enunț: GRANT ON *.* către 'joe'@'%' acoperă și biroul, deci un singur SHOW GRANTS ajunge?
    Capcană: „% este toate hosturile, deci lista este una.”
    Cheie: '%' este cel mai puțin specific. Un rând mai specific poate fi contul potrivit la conectare. SHOW GRANTS pentru office și pentru home rămân citiri separate.
    Demonstrație: MySQLSecurity_Staff_Interview.separateFromGrant()
47. Enunț: Ești într-un driver și vrei reîncărcarea. Trimiți mysqladmin sau FLUSH PRIVILEGES?
    Capcană: „mysqladmin este propoziția, pentru că face același lucru.”
    Cheie: Driverul trimite FLUSH PRIVILEGES. mysqladmin este program. Propoziția este depreciată: o folosești după o scriere directă sau după --skip-grant-tables, nu după GRANT.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
48. Enunț: Vrei pluginul și parola contului, nu privilegiile. SHOW GRANTS este propoziția?
    Capcană: „SHOW GRANTS listează și parola, fiindcă contul este același.”
    Cheie: SHOW GRANTS listează privilegii și, la un partial revoke, linii REVOKE. Parola nu stă în clar în authentication_string. Pentru privilegii rămâi la SHOW GRANTS.
    Demonstrație: MySQLSecurity_Staff_Interview.recognizeShowGrants()
49. Enunț: Poți rula FLUSH PRIVILEGES ca să „doar reîncarci”, cu o tranzacție de salarii încă deschisă?
    Capcană: „Reîncărcarea nu atinge tranzacția de aplicație.”
    Cheie: Nu. FLUSH face commit implicit. SHOW GRANTS nu este pe lista aceea și poate fi citit fără să închidă tranzacția.
    Demonstrație: MySQLSecurity_Staff_Interview.effectOnGrantMemory()
50. Enunț: Un cont de facturare trebuie să citească payroll și nimic din schema mysql. Ce listezi la sfârșit și ce nu pui în user?
    Capcană: „Pui Select_priv în mysql.user și gata, SHOW GRANTS este opțional.”
    Cheie: Nu scrii direct în mysql.user și nu lași privilegiu global pentru un cont de aplicație. GRANT la nivel de schemă sau tabelă, apoi SHOW GRANTS pentru hostul lui. Dacă are global și trebuie să piardă mysql, partial revoke apare ca linie REVOKE. Rândul unui angajat rămâne regulă de aplicație.
    Demonstrație: MySQLSecurity_Staff_Interview.chooseGrantOrApplication()
*/

/*
🟢 Începător
    Ce este            banca care întreabă cine citește privilegiile și cine le scrie
    De ce e nevoie     fără ea, UPDATE pe mysql.user pare GRANT
🟡 Producție
    Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
🔴 Staff
    Unde               cincizeci de întrebări rămân în comentariu; metodele întorc propoziția, fără main
Pași                   1. recognizeShowGrants
                       2. effectOnGrantMemory
                       3. separateFromGrant
                       4. failDirectUpdate
                       5. chooseGrantOrApplication
*/
// Începător: clasa ține întrebările și textul pe care îl alegi între citire și scriere.
// Producție: nu are main. Propozițiile stau în metodele de mai jos și în MySQL_Security.
// Expert: banca nu predă conexiunea criptată și nu predă firul din CONNECTION_ID(). Cincizeci de întrebări rămân în comentariu.
public final class MySQLSecurity_Staff_Interview
// Începător: de aici încep metodele care întorc textul întrebărilor.
// Producție: fără acoladă, cele cinci metode nu au clasă.
// Expert: acolada nu deschide o sesiune și nu acordă RELOAD.
{

    /*
    🟢 Începător
        Ce este            textul pe care îl recunoști ca listă de privilegii pentru un host
        De ce e nevoie     fără el, orice joe pare același cont
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
    🔴 Staff
        Unde               lista vine la sfârșitul propoziției; rolul din ea nu este activ
    Pași                   1. întoarce showGrantsAtOffice
    */
    // Începător: metoda dă propoziția pe care o recunoști ca citire de drepturi pentru birou.
    // Producție: întoarce SHOW GRANTS FOR 'joe'@'office.example.com'; . Dacă dispare, întrebările 1, 2, 4, 9, 10, 15, 17, 19, 20, 26, 29, 30, 33, 40 și 48 nu mai au textul.
    // Expert: nu este GRANT și nu este SHOW CREATE USER. Nu garantează că partial_revokes este pornit.
    public static String recognizeShowGrants()
    // Începător: înăuntru pleacă propoziția de recunoscut.
    // Producție: fără acoladă, SHOW GRANTS pentru office nu iese.
    // Expert: acolada nu face commit implicit.
    {
        // Începător: înapoi pleacă cererea de listă, gata de trimis la server.
        // Producție: return MySQL_Security.showGrantsAtOffice() predă SHOW GRANTS FOR 'joe'@'office.example.com'; . Fără linie, recunoașterea nu are propoziție.
        // Expert: un privilegiu din rezultat este în vigoare; un rol nu. Linia nu taie un rând din payroll.
        return MySQL_Security.showGrantsAtOffice();
    // Începător: recunoașterea se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu activează rolul implicit.
    }

    /*
    🟢 Începător
        Ce este            reîncărcarea care schimbă momentul în care dreptul se vede
        De ce e nevoie     ca să nu crezi că discul și memoria sunt același lucru
    🟡 Producție
        Cum se scrie       FLUSH PRIVILEGES;
    🔴 Staff
        Unde               commit implicit; tabelă la cererea următoare, bază la USE, global static la conexiune nouă
    Pași                   1. întoarce reloadGrantTables
    */
    // Începător: metoda dă propoziția care pune din nou drepturile în memorie.
    // Producție: întoarce FLUSH PRIVILEGES; . Dacă dispare, întrebările 6, 8, 12, 13, 16, 18, 27, 28, 34, 37, 38, 39, 47 și 49 nu mai au textul.
    // Expert: nu este necesară după GRANT. În 9.7 este depreciată. Nu acordă un privilegiu care nu era scris.
    public static String effectOnGrantMemory()
    // Începător: înăuntru pleacă reîncărcarea, nu acordarea.
    // Producție: fără acoladă, FLUSH PRIVILEGES nu iese din bancă.
    // Expert: acolada nu pornește serverul cu --skip-grant-tables.
    {
        // Începător: serverul recitește drepturile și poate închide tranzacția deschisă.
        // Producție: return MySQL_Security.reloadGrantTables() predă FLUSH PRIVILEGES; . Fără linie, efectul asupra memoriei nu are propoziție.
        // Expert: cu --skip-grant-tables, abia această propoziție pornește verificarea. Privilegiul global static tot așteaptă o conexiune nouă.
        return MySQL_Security.reloadGrantTables();
    // Începător: metoda memoriei se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu scoate deprecierea și nu eliberează ea cache-ul caching_sha2_password.
    }

    /*
    🟢 Începător
        Ce este            celălalt host, ca să vezi că GRANT către un nume nu este această listă
        De ce e nevoie     fără el, un singur joe adună drepturile de la birou și de acasă
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'home.example.com';
    🔴 Staff
        Unde               alt cont; GRANT scrie, această propoziție citește
    Pași                   1. întoarce showGrantsAtHome
    */
    // Începător: metoda dă lista contului de acasă, nu scrierea privilegiului.
    // Producție: întoarce SHOW GRANTS FOR 'joe'@'home.example.com'; . Dacă dispare, întrebările 7, 21, 22, 23, 24, 25 și 46 nu mai au perechea de host.
    // Expert: nu este GRANT ALL ON db1.*. Nu promite că '%' a fost contul potrivit la conectare.
    public static String separateFromGrant()
    // Începător: înăuntru pleacă celălalt cont.
    // Producție: fără acoladă, SHOW GRANTS pentru home nu iese.
    // Expert: acolada nu scrie privilegiul și nu reîncarcă memoria.
    {
        // Începător: serverul poate întoarce alte drepturi decât la birou.
        // Producție: return MySQL_Security.showGrantsAtHome() predă SHOW GRANTS FOR 'joe'@'home.example.com'; . Fără linie, deosebirea de GRANT nu are al doilea cont.
        // Expert: CREATE USER poate lăsa etapa 1 să reușească și lista aceasta tot poate să nu conțină SELECT. TLS nu schimbă lista.
        return MySQL_Security.showGrantsAtHome();
    // Începător: metoda vecinului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu mută propoziția în folderul GRANT.
    }

    /*
    🟢 Începător
        Ce este            scrierea directă care lasă verificarea pe dreptul vechi
        De ce e nevoie     ca să nu o rulezi când voiai ca SELECT să treacă
    🟡 Producție
        Cum se scrie       UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey';
    🔴 Staff
        Unde               discul se poate schimba; sesiunea deschisă nu primește privilegiul global static
    Pași                   1. întoarce directGrantTableUpdate
    */
    // Începător: metoda dă propoziția care nu publică dreptul.
    // Producție: întoarce UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Dacă dispare, întrebările 5, 11, 14, 31, 32, 35 și 36 nu mai au textul.
    // Expert: calea este depreciată în 9.7. Nu este GRANT. Nu filtrează rândurile din payroll.
    public static String failDirectUpdate()
    // Începător: înăuntru pleacă scrierea care ocolește GRANT.
    // Producție: fără acoladă, UPDATE pe mysql.user nu iese din bancă.
    // Expert: acolada nu reîncarcă memoria.
    {
        // Începător: dreptul de citire poate fi marcat pe disc și rămâne nefolosit la verificarea următoare.
        // Producție: return MySQL_Security.directGrantTableUpdate() predă UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Fără linie, eșecul nu are propoziție.
        // Expert: după o reîncărcare ulterioară, privilegiul global static tot așteaptă o conexiune nouă. Un DELETE global în user ar trece înainte de tables_priv.
        return MySQL_Security.directGrantTableUpdate();
    // Începător: metoda eșecului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu pune Select_priv înapoi pe 'N'.
    }

    /*
    🟢 Începător
        Ce este            citirea pe care o păstrezi când privilegiul este pe tabelă, nu pe un rând
        De ce e nevoie     ca să nu ceri unui privilegiu să ascundă rândul unui singur pacient
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
    🔴 Staff
        Unde               treci la GRANT când lista nu conține privilegiul; rândul rămâne în aplicație
    Pași                   1. întoarce showGrantsAtOffice
    */
    // Începător: metoda dă citirea la care rămâi când vrei să știi dacă contul poate rula propoziția.
    // Producție: întoarce SHOW GRANTS FOR 'joe'@'office.example.com'; . Dacă dispare, întrebările 41, 42, 43, 44, 45 și 50 nu mai au lista.
    // Expert: nu deblochează account_locked. Nu înlocuiește un view care filtrează rândul. partial revoke se vede aici doar ca linie REVOKE, după ce a reușit.
    public static String chooseGrantOrApplication()
    // Începător: înăuntru pleacă lista, nu regula de pe un rând.
    // Producție: fără acoladă, alegerea nu are metodă.
    // Expert: acolada nu face GRANT la nivel de schemă.
    {
        // Începător: serverul spune ce drepturi sunt scrise pentru birou. Rândurile de salarii rămân.
        // Producție: return MySQL_Security.showGrantsAtOffice() predă SHOW GRANTS FOR 'joe'@'office.example.com'; . Fără linie, nu rămâne propoziția la care te întorci după GRANT.
        // Expert: dacă lista nu conține SELECT pe payroll, treci la GRANT. Dacă regula este „doar rândul acestui pacient”, rămâi în aplicație. Linia nu pune privilegiu global în user.
        return MySQL_Security.showGrantsAtOffice();
    // Începător: metoda alegerii se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu șterge întrebările din comentariu și nu închide sesiunea.
    }
// Începător: banca se termină aici, fără un main care să arate cheile.
// Producție: fără închidere, cele cinci metode nu compilează.
// Expert: închiderea nu reduce cele 50 de întrebări și nu predă un alt folder din catalog.
}
