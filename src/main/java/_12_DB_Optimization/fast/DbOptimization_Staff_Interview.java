/*
TOPIC:
    Cincizeci de întrebări despre alegerea pe shipment: planul coletelor deschise, indexul i_status sau predicatul pe care serverul îl poate folosi.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Recunoașterea din EXPLAIN, din type ALL sau din key NULL, momentul în care rândurile rămân sau pot dispărea, deosebirea de pliere și de FORCE INDEX, eșecul din EXPLAIN ANALYZE pe DELETE și alegerea față de aplicație.
MECHANISM:
    EXPLAIN pe status = 'open' arată cum ar rula serverul și nu scrie rânduri. CREATE INDEX i_status schimbă schema, nu statusul stocat. EXPLAIN ANALYZE pe DELETE multi-tabelă rulează ștergerea. AND 1 = 1 este scos la pregătire. YEAR(shipped_at) poate fi chemat pe rând.
LOOKALIKES:
    Plierea parantezelor nu este i_status. FORCE INDEX nu creează indexul și presupune scanarea foarte scumpă. Query cache nu este în lista din 10.1. FULLTEXT pe InnoDB nu este B-tree-ul de pe status.
BOOK THEORY:
    Manualul 9.7, 10.1, 10.2, 10.2.1, 10.2.1.1, 10.2.1.2, 10.2.1.23, 10.3.1, 10.8.2, 10.9.4, 10.10, 15.1.18 și 15.8.2, deschise în tur. query-cache.html pe 9.7 a răspuns 404. open_books.py nu a găsit niciun PDF.
BOOK EXAMPLE:
    Probele din Database_Optimization, Index_Optimization și SQL_STATEMENTS_OPTIMIZATION_, nu o metodă din această clasă.
REPO DEMO:
    src/main/java/_12_DB_Optimization/Database_Optimization.java
BREAK LAB:
    EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open', sau FORCE INDEX (i_status) înainte de plan.
TRADE-OFFS:
    i_status când egalitatea pe status nu are restricție utilizabilă pe o tabelă mare. Textul pliat când diferența este 1 = 1. Aplicația când filtrul nu este o coloană din shipment.
ANTI-PATTERNS:
    EXPLAIN ANALYZE pe DELETE-ul multi-tabelă, FORCE INDEX înainte de EXPLAIN, sau un index pe status crezut acoperire pentru shipped_at.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească singur.
NOVEL TRANSFER:
    Biletul de ghișeu cu status deschis: același plan, nu tabela shipment. Ștergerea măsurată tot poate goli biletele deschise.
SPACED REVIEW:
    O zi: întrebările 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei optimizări (optimization):
       Optimizarea aici este alegerea citită din plan: index utilizabil sau propoziție lăsată în forma pe care serverul o pliază. EXPLAIN nu schimbă rândurile din shipment.

Exemplu:
       DbOptimization_Terms.explainOptimization()

Definiția unui index (index):
       Indexul i_status este structura de pe status. Dacă planul nu îl alege, căutarea tot poate citi tabela.

Exemplu:
       DbOptimization_Terms.explainIndex()

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: EXPLAIN SELECT id FROM shipment WHERE status = 'open' arată type ALL și key NULL. Ce pârghie recunoști?
       Capcană: ALL înseamnă că serverul a șters coletele închise, iar key NULL înseamnă că i_status există și a fost ignorat din greșeală de client.
       Cheie: type ALL este scanarea tabelei. key NULL înseamnă că nu s-a ales un index. Pe o tabelă mare, fără restricție utilizabilă pe status, pârghia este CREATE INDEX i_status, nu rescrierea lui AND 1 = 1. În tur planul nu a fost citit: 1045.
    2. Enunț: possible_keys este NULL la același EXPLAIN. Ce spune 10.8.2 despre coloana asta, separat de key?
       Capcană: possible_keys NULL înseamnă că optimizerul a ales scanarea deși avea i_status în listă.
       Cheie: possible_keys NULL înseamnă că nu există index relevant. Abia atunci creezi indexul și ceri EXPLAIN din nou. key NULL înseamnă că niciun index nu a fost ales, chiar dacă lista nu era goală.
    3. Enunț: Vezi SELECT id FROM shipment WHERE (status = 'open' AND 1 = 1). Ce mecanism este textul?
       Capcană: Parantezele și 1 = 1 sunt un index nou, pentru că textul este mai lung.
       Cheie: 10.2.1.1 scoate parantezele inutile și condiția constantă la pregătire, ca pe 5=5 din exemplu. Nu se creează i_status. Coletele deschise rămân aceleași colete.
    4. Enunț: Ce recunoști în EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open'?
       Capcană: Cuvântul EXPLAIN oprește ștergerea, deci este doar planul din planTheServerWouldUse.
       Cheie: 15.8.2: EXPLAIN ANALYZE rulează propoziția. Forma merge pe DELETE multi-tabelă. Coletele deschise pot lipsi la sfârșitul ei. EXPLAIN fără ANALYZE, pe SELECT, nu este această propoziție.
    5. Enunț: DESCRIBE shipment și EXPLAIN SELECT id FROM shipment WHERE status = 'open'. Ce deosebește practica, dacă parserul le tratează ca sinonime?
       Capcană: DESCRIBE nu poate arăta niciodată un plan, pentru că sinonimul este interzis.
       Cheie: 15.8.2: parserul le tratează ca sinonime. Pe un nume de tabelă, practica este lista de coloane. Pe un SELECT, practica lui EXPLAIN este planul. EXPLAIN shipment nu este planul căutării după status.
    6. Enunț: WHERE status LIKE '%en'. Ce face extragerea de range cu tiparul?
       Capcană: i_status îl folosește ca interval, pentru că status este indexat și % este doar un caracter.
       Cheie: 10.2.1.2 scoate LIKE care începe cu wildcard, ca key1 LIKE '%b', și îl înlocuiește cu TRUE ca să nu piardă rânduri. Nu este status = 'open' și nu este LIKE 'open%'.
    7. Enunț: WHERE YEAR(shipped_at) = 2026. Ce nu este această formă, față de definiția de range?
       Capcană: YEAR este o constantă, deci shipped_at este comparat cu 2026 și indexul pe dată este obligatoriu.
       Cheie: 10.2.1.2 definește range-ul ca comparația unei părți de cheie cu o constantă. Aici funcția stă pe coloană. 10.2.1 spune că funcția poate fi chemată pentru fiecare rând. Pagina nu spune că un index pe expresie este interzis.
    8. Enunț: WHERE shipped_at >= '2026-01-01' AND shipped_at < '2027-01-01'. Ce formă este, pe BTREE?
       Capcană: Două inegalități nu pot fi range, fiindcă range-ul este doar egalitatea.
       Cheie: Pe BTREE, comparația părții de cheie cu o constantă prin >= și < este condiție de range. Nu garantează că planul o alege: dacă intervalul acoperă prea mult, 10.2.1.23 permite scanarea.
    9. Enunț: Extra spune Using index la EXPLAIN SELECT status FROM shipment WHERE status = 'open'. Ce s-a citit?
       Capcană: Using index înseamnă că s-a creat i_status în această propoziție și că shipped_at a venit tot din arbore.
       Cheie: 10.8.2: valorile cerute au venit din arborele indexului, nu s-a creat indexul acum. status poate sta în i_status. shipped_at nu este în acel index, deci SELECT shipped_at nu este același semn.
    10. Enunț: ERROR 1064 la EXPLAIN DELETE FROM shipment USE INDEX (i_status) WHERE status = 'open'. De ce cade sintaxa?
        Capcană: Orice DELETE refuză hint-ul, deci și DELETE s FROM shipment s USE INDEX (i_status) cade la fel.
        Cheie: 10.9.4: hint-ul de index pe DELETE merge doar în forma multi-tabelă. DELETE FROM shipment USE INDEX este eroarea 1064 din exemplu. Nu este 1045 de la autentificare.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: Când vezi că EXPLAIN SELECT id FROM shipment WHERE status = 'open' nu a șters coletele deschise?
        Capcană: La COMMIT. Până atunci rândurile sunt șterse în tranzacție.
        Cheie: La sfârșitul lui EXPLAIN ai un plan, nu o scriere. 15.8.2 spune că EXPLAIN arată cum ar procesa serverul propoziția. Rândurile rămân. În tur nici planul nu a fost văzut.
    12. Enunț: Când se vede i_status în schemă, și ce nu se schimbă în rânduri?
        Capcană: La următorul SELECT, iar statusul 'open' este rescris în 'indexed'.
        Cheie: La sfârșitul lui CREATE INDEX, mapat pe ALTER TABLE. Valorile status rămân. 15.1.18: CREATE INDEX nu creează PRIMARY KEY. Pe InnoDB cheia primară este așezarea rândurilor, nu i_status.
    13. Enunț: EXPLAIN ANALYZE pe DELETE-ul multi-tabelă al coletelor deschise. Când pot lipsi rândurile?
        Capcană: Niciodată, pentru că ANALYZE doar măsoară și lasă tabela neatinsă până la un DELETE separat.
        Cheie: La sfârșitul propoziției care chiar rulează. 15.8.2 spune că EXPLAIN ANALYZE rulează propoziția. Nu este momentul lui EXPLAIN fără ANALYZE.
    14. Enunț: shipment are 6 rânduri scurte și type este ALL. Ce spune 10.2.1.23 despre tabela mică?
        Capcană: ALL este interzis sub 10 rânduri, deci i_status este obligatoriu.
        Cheie: Pe o tabelă atât de mică, scanarea este adesea potrivită și costul este neglijabil. Sub 10 rânduri, cu rând scurt, este chiar cazul citat. Indexul nu este prima pârghie.
    15. Enunț: Optimizerul calculează că status = 'open' acoperă prea mult din shipment. Ce poate alege, deși i_status există?
        Capcană: key nu poate fi NULL dacă possible_keys îl conține pe i_status.
        Cheie: Poate alege scanarea. 10.2.1.23 o numește când constantele acoperă prea mult din tabelă. 10.2.1.1: pragul fix de 30% nu mai decide. key NULL rămâne posibil.
    16. Enunț: status are cardinalitate mică, aproape toate coletele sunt deschise. Ce presupunere face serverul?
        Capcană: Cardinalitatea mică înseamnă index mai selectiv, deci ref este obligatoriu.
        Cheie: Multe rânduri pentru aceeași valoare înseamnă multe căutări prin cheie, iar scanarea poate fi mai rapidă. 10.3.1 spune același lucru când propoziția are nevoie de majoritatea rândurilor.
    17. Enunț: InnoDB, i_status secundar, SELECT id, status WHERE status = 'open'. De ce id poate veni din index fără să fie declarat în i_status?
        Capcană: id vine din index doar dacă l-ai pus tu în CREATE INDEX, altfel se citește tot rândul.
        Cheie: 10.8.2 și 10.3.1: indexul secundar InnoDB ține și cheia primară. id poate fi acoperit odată cu status. shipped_at nu este copiat din acest motiv.
    18. Enunț: innodb_stats_persistent este pornit și tocmai ai creat i_status. Ce propoziție cere 15.1.18 înainte să te bazezi pe estimare?
        Capcană: EXPLAIN ANALYZE DELETE, ca să și măsori, și să cureți coletele deschise.
        Cheie: ANALYZE TABLE shipment, ca să actualizeze distribuția cheilor. Nu este ștergerea. Nu înlocuiește citirea lui key din EXPLAIN.
    19. Enunț: Un index (status, shipped_at) și WHERE shipped_at = '2026-10-20', fără condiție pe status. Este prefixul din stânga?
        Capcană: Orice coloană din index este prefix, deci căutarea sare la dată.
        Cheie: 10.3.1: se poate folosi prefixul din stânga. shipped_at singur nu este acel prefix. Skip scan este altă metodă, când condițiile ei sunt îndeplinite, nu este egalitatea pe status.
    20. Enunț: Cache-ul de prepared statements din 10.10.3 ține structura internă. Când dispare, și schimbă un SELECT rândurile?
        Capcană: Ține rezultatul coletelor deschise pentru toate sesiunile, până la FLUSH QUERY CACHE.
        Cheie: Este pe sesiune și se aruncă la sfârșitul sesiunii. Un SELECT nu schimbă metadatele și nu schimbă rândurile. Nu este query cache: pagina lui pe 9.7 este 404, iar 10.1 nu îl listează.

Deosebire de mecanismul vecin:
    21. Enunț: De ce (status = 'open' AND 1 = 1) nu este CREATE INDEX i_status?
        Capcană: Ambele schimbă planul, deci sunt aceeași pârghie pe shipment.
        Cheie: Plierea scoate condiția constantă și nu adaugă structură. CREATE INDEX adaugă i_status în schemă și nu schimbă statusul stocat. Fără EXPLAIN nu știi dacă noul index este ales.
    22. Enunț: De ce YEAR(shipped_at) = 2026 nu este i_status?
        Capcană: Orice funcție pe un colet folosește indexul de pe status.
        Cheie: i_status este pe status. Funcția este pe shipped_at și poate rula pe fiecare rând. Egalitatea status = 'open' este alt WHERE.
    23. Enunț: De ce FORCE INDEX (i_status) nu este CREATE INDEX i_status?
        Capcană: FORCE creează indexul și îl impune, deci schema și planul se schimbă împreună.
        Cheie: Hint-ul presupune că scanarea este foarte scumpă și nu creează numele. 10.9.4: hint-ul de optimizer INDEX este echivalent și este destinat să îl înlocuiască. USE INDEX amestecat cu FORCE INDEX pe aceeași tabelă este eroare.
    24. Enunț: De ce EXPLAIN SELECT nu este EXPLAIN ANALYZE DELETE s FROM shipment s?
        Capcană: Ambele încep cu EXPLAIN, deci niciuna nu poate scoate un rând.
        Cheie: Primul arată cum ar rula căutarea. Al doilea rulează ștergerea multi-tabelă. Coletele deschise pot lipsi doar la al doilea.
    25. Enunț: De ce query cache nu este buffer pool-ul din 10.1?
        Capcană: Comentariul vechi le pune în aceeași listă, deci în 9.7 se configurează la fel.
        Cheie: 10.1, deschis în tur, numește buffer pool-ul InnoDB și key cache-ul MyISAM. Nu numește query cache. 10.10 adaugă cache-ul de prepared statements. 10.2.1 încă are fraza „MySQL query cache”, deci manualul nu este uniform. Pagina query-cache.html a răspuns 404.
    26. Enunț: De ce un JOIN între shipment și alt tabel nu este alegerea din acest folder?
        Capcană: type ALL pe ambele tabele este produsul din exemplul de EXPLAIN, deci optimizarea de aici este un JOIN.
        Cheie: Exemplul cu produs cartezian din 10.8.2 este alt plan. Aici alegi între i_status pe o tabelă și forma predicatului. Un JOIN nu creează i_status și nu pliază 1 = 1.
    27. Enunț: De ce HAVING status = 'open' nu este această optimizare?
        Capcană: HAVING filtrează după grup, deci înlocuiește și indexul, și EXPLAIN.
        Cheie: 10.2.1.1 spune că HAVING se unește cu WHERE când nu ai GROUP BY și nici agregat. Nu este CREATE INDEX și nu este semnul Using index.
    28. Enunț: De ce FULLTEXT pe status nu este i_status?
        Capcană: Orice index pe InnoDB este B-tree, deci FULLTEXT caută status = 'open' la fel.
        Cheie: 10.3.1: pe InnoDB, FULLTEXT este listă inversată, nu B-tree. i_status pentru egalitate este INDEX obișnuit. Hint-urile de index sunt ignorate în liniste la căutarea FULLTEXT în mod natural.
    29. Enunț: De ce rescrierea numelui coloanei din shipped_at în shippedAt nu este planul?
        Capcană: Convenția de nume schimbă key în EXPLAIN.
        Cheie: Convenția nu este indexul și nu este predicatul. Planul se citește din EXPLAIN. Un comentariu sau o rupere de rând nu umple key cu i_status.
    30. Enunț: De ce o verificare în aplicație, înainte de SELECT, nu este i_status?
        Capcană: Aplicația știe care colete sunt deschise, deci serverul nu mai citește tabela.
        Cheie: Doi clienți pot vedea alt status până la propoziția de pe server. Indexul este structura din plan. Aplicația rămâne când „deschis” nu este coloana status, ci o regulă din alt sistem.

Eșec în producție:
    31. Enunț: Pui i_status și lași WHERE status LIKE '%en'. Ce nu devine interval?
        Capcană: După CREATE INDEX, orice LIKE pe status este range, deci type ALL dispare.
        Cheie: Tiparul care începe cu wildcard este scos din range. Indexul pe status nu îl transformă în comparație cu o constantă. key poate rămâne NULL. Nu este o eroare de sintaxă.
    32. Enunț: Rulezi EXPLAIN ANALYZE DELETE s FROM shipment s WHERE s.status = 'open' ca să „doar vezi planul” în producție. Ce colete pot lipsi?
        Capcană: Niciunul. ANALYZE nu are voie să șteargă.
        Cheie: Cele cu status deschis, la sfârșitul propoziției care a rulat. Simptomul nu este un rând type ALL. În tur șirul nu a fost trimis.
    33. Enunț: FORCE INDEX (i_status) înainte de orice EXPLAIN, pe o tabelă de 6 rânduri. Ce presupunere ai băgat în optimizer?
        Capcană: Nimic. FORCE doar afișează indexul, ca EXPLAIN.
        Cheie: Că scanarea este foarte scumpă. Pe tabela mică, 10.2.1.23 spune că scanarea este adesea potrivită. Hint-ul nu creează i_status dacă el nu există.
    34. Enunț: CREATE INDEX i_shipped ON shipment (shipped_at), iar WHERE rămâne YEAR(shipped_at) = 2026. De ce planul poate rămâne ALL?
        Capcană: Indexul pe coloană face funcția să citească arborele, deci ALL este imposibil.
        Cheie: Predicatul nu este comparația lui shipped_at cu o constantă. Fără restricție utilizabilă, type ALL este scanarea. Intervalul shipped_at >= '2026-01-01' AND shipped_at < '2027-01-01' este forma de range, nu YEAR.
    35. Enunț: SELECT id, status, shipped_at FROM shipment, fără WHERE, după ce i_status există. De ce covering-ul nu este semnul așteptat?
        Capcană: i_status acoperă orice SELECT, fiindcă status este în index.
        Cheie: Nu există restricție pe status. shipped_at nu este în i_status. 10.3.1: când se cer aproape toate rândurile, citirea secvențială poate fi mai rapidă. Using index nu este acest text.
    36. Enunț: USE INDEX (i_status) FORCE INDEX (i_status) pe shipment, în același SELECT. Ce eroare este forma?
        Capcană: Serverul le împacă și folosește indexul de două ori, deci planul este mai bun.
        Cheie: 10.9.4: amestecul de USE INDEX și FORCE INDEX pe aceeași tabelă este eroare. Nu este 1045. Nu creează indexul.
    37. Enunț: Lași comentariul că query cache ține rezultatul și nu mai citești EXPLAIN. Ce s-a configurat greșit față de 10.1?
        Capcană: Query cache este a treia zonă de memorie din 9.7, lângă buffer pool și key cache.
        Cheie: 10.1 nu o listează. 10.10 listează buffer pool, key cache și cache-ul de prepared statements, nu rezultatul interogării între sesiuni. 10.2.1 încă numește query cache, deci nu cita fraza aceea ca lista curentă.
    38. Enunț: Compari status, coloană utf8mb4, cu o coloană latin1 din alt tabel. Ce spune 10.3.1 despre index?
        Capcană: Seturile diferite sunt doar afișare, iar i_status tot se folosește.
        Cheie: Comparația între coloane text cu seturi diferite poate împiedica indexul. Nu este același caz cu status = 'open', constantă în propoziție.
    39. Enunț: CREATE UNIQUE INDEX pe status, ca să „grăbești” coletele deschise. Ce refuză schema și nu îți trebuia?
        Capcană: UNIQUE este doar un B-tree mai rapid. Al doilea colet deschis rămâne.
        Cheie: UNIQUE refuză al doilea rând cu același status. Două colete deschise nu mai încap. i_status fără UNIQUE nu refuză valoarea. Nu este cheia primară.
    40. Enunț: EXPLAIN FOR CONNECTION arată rows NULL și filtered NULL după un FORCE INDEX care a sărit index dive. Ce ai măsurat greșit dacă citești rows ca zero?
        Capcană: Zero rânduri înseamnă că shipment nu are colete deschise.
        Cheie: 10.2.1.2, în 9.7: când dive-ul este sărit, rows și filtered sunt NULL în ieșirea tradițională pentru EXPLAIN FOR CONNECTION. NULL nu este un număr de colete. Fără FOR CONNECTION, ieșirea lui EXPLAIN nu se schimbă din acest motiv.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: status = 'open' pe o tabelă mare, possible_keys NULL. Ce alegi și ce nu garantează?
        Capcană: Rescrii în AND 1 = 1 și te oprești.
        Cheie: CREATE INDEX i_status, apoi EXPLAIN din nou. Nu garantează key = i_status: scanarea poate rămâne dacă valoarea acoperă prea mult. În tur șirul nu a plecat.
    42. Enunț: Singura diferență față de căutarea corectă este AND 1 = 1. Ce lași pe server?
        Capcană: Ștergi parantezele în aplicație, altfel planul nu există.
        Cheie: Lași textul. 10.2.1.1 îl pliază la pregătire. Nu adaugi i_status doar pentru 1 = 1. Nu este un motiv de FORCE INDEX.
    43. Enunț: Vrei coletele din 2026 și ai YEAR(shipped_at) = 2026. Ce formă îi dai intervalului, și când tot lași scanarea?
        Capcană: YEAR rămâne, iar i_status pe status rezolvă anul.
        Cheie: shipped_at >= '2026-01-01' AND shipped_at < '2027-01-01', comparație de cheie cu constante. Dacă intervalul acoperă aproape toată tabela, scanarea poate fi alegerea. i_status nu este coloana datei.
    44. Enunț: Când rămâi la scanare, fără index nou?
        Capcană: Niciodată. type ALL se repară întotdeauna cu FORCE INDEX.
        Cheie: Tabelă mică, sau propoziție care citește majoritatea rândurilor, sau cardinalitate mică pe status. 10.3.1 și 10.2.1.23. FORCE INDEX presupune scanarea foarte scumpă și nu este primul pas.
    45. Enunț: „Deschis” se calculează în aplicație dintr-un sistem de afară, nu din coloana status. Unde lași regula?
        Capcană: Creezi i_status pe o coloană care nu există în shipment, iar EXPLAIN o va găsi.
        Cheie: În aplicație. Propoziția nu vede regula. Un index pe status nu filtrează un fapt care nu este în tabelă. Planul nu înlocuiește sistemul din afară.
    46. Enunț: Ai deja i_status și EXPLAIN arată că folosește un alt index, greșit pentru tine. Ce hint este înlocuitorul așteptat al lui FORCE INDEX?
        Capcană: FORCE INDEX rămâne forma nouă din 9.7 și INDEX() este doar un comentariu.
        Cheie: Hint-ul de optimizer INDEX, echivalent cu FORCE INDEX și destinat să îl înlocuiască. 10.9.4 spune că FORCE INDEX este de așteptat să fie scos mai târziu. Tot nu creează indexul și tot se pune după ce ai citit planul.
    47. Enunț: Statisticile sunt vechi după un import mare, iar planul alege ALL. Ce rulezi înainte de încă un index?
        Capcană: EXPLAIN ANALYZE DELETE, ca să golești tabela și să o faci mică.
        Cheie: ANALYZE TABLE shipment. 10.2.1.23 îl dă ca să actualizeze distribuția cheilor. Un index nou nu repară o statistică necitită. Ștergerea nu este măsurătoarea.
    48. Enunț: Trebuie să vezi planul unei ștergeri fără să pierzi coletele deschise. Ce formă nu folosești?
        Capcană: EXPLAIN ANALYZE este singura formă care arată un plan de DELETE.
        Cheie: Nu folosi EXPLAIN ANALYZE pe DELETE-ul multi-tabelă. EXPLAIN pe SELECT arată planul căutării fără să ruleze ștergerea. În tur oricum nu s-a trimis șirul.
    49. Enunț: LIKE '%en' este o cerință reală, terminația chiar contează. Ce nu promiți cu i_status?
        Capcană: Indexul face terminația la fel de ieftină ca status = 'open'.
        Cheie: Nu promiți range pe acel tipar. Extragerea îl înlocuiește cu TRUE. Dacă terminația este rară, tot nu ai semnul din 10.2.1.2 că este interval. Egalitatea rămâne altă propoziție.
    50. Enunț: La ghișeu, biletul are status deschis, nu este un colet din shipment. Ce păstrezi din alegere și ce nu copiezi?
        Capcană: Copiezi numele tabelei shipment, iar fără acest nume planul nu există.
        Cheie: EXPLAIN înainte de index. type ALL și key NULL, fără restricție utilizabilă, duc la un index pe coloana de status a biletului. AND 1 = 1 tot nu este indexul. EXPLAIN ANALYZE pe ștergerea biletelor deschise tot poate să le șteargă. Numele shipment nu este mecanismul.

🟢 Începător
    Ce este            banca de interviu pentru alegerea dintre plan, i_status și predicat
    De ce e nevoie     fără cele cincizeci, type ALL și EXPLAIN ANALYZE rămân aceeași poveste
🟡 Producție
    Cum se scrie       cincizeci de enunțuri în comentariu, fără main
🔴 Staff
    Unde               cheia stă aici. Proba care ține SQL-ul stă în clasele de topic. Această clasă nu o trimite.
Pași
    1. Citește enunțul.
    2. Cheia este deja scrisă, pentru artefact.
    3. Nu porni un main de recall.
*/
// Începător: banca stă lângă lecția scurtă și nu șterge un colet.
// Producție: package _12_DB_Optimization.fast. Nu are main de recall.
// Expert: Cele cincizeci sunt despre planul de pe shipment. Nu se lipesc pe WHERE sau pe JOIN.
package _12_DB_Optimization.fast;

// Începător: clasa ține întrebările și cheile în comentariul de deasupra.
// Producție: public final class DbOptimization_Staff_Interview. Dacă primește main, nu mai este banca fără recall.
// Expert: Nu execută DELETE-ul măsurat. Efectul pe server rămâne NERULAT în tur.
public final class DbOptimization_Staff_Interview {
    // Începător: nu există o propoziție în corp.
    // Producție: acolada închide DbOptimization_Staff_Interview. Sub ea nu se adaugă un main.
    // Expert: Cele cincizeci rămân în comentariu. Golul nu șterge o cheie.
}
