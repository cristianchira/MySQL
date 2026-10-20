/*
TOPIC:
    Cincizeci de întrebări despre cererea SELECT data_value FROM entity_row și despre planul pe care îl face optimizerul.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. root@localhost a primit Access denied. EXPLAIN nu a fost citit. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Recunoaștere din SELECT sau din buclă, momentul rezultatului, deosebirea de celulă și de pașii scriși, eșecul buclei și alegerea față de aplicație.
MECHANISM:
    SELECT data_value FROM entity_row numește rezultatul. Optimizerul construiește planul. FOR EACH entity_row RETURN data_value nu pornește.
LOOKALIKES:
    Celula stocată. Bucla. Clasificarea DDL, DML, DCL și TCL.
BOOK THEORY:
    Manualul 9.7, 10.2.1: SELECT face căutarea, ANALYZE TABLE hrănește planul, EXPLAIN arată indexul. PDF-ul: optimizerul alege abordarea între TRUNCATE și DELETE.
BOOK EXAMPLE:
    SQL_Declarative_Language.namedResult și SQL_Declarative_Language.proseLoopTheServerRejects
REPO DEMO:
    src/main/java/_1_SQL/_2_SQL_Declarative_Language/SQL_Declarative_Language.java
BREAK LAB:
    FOR EACH entity_row RETURN data_value
TRADE-OFFS:
    Aplicația pentru o listă locală. Celula pentru ce rămâne. Folderul de propoziții pentru clasificare.
ANTI-PATTERNS:
    Bucla sau comentariul despre optimizer, trimise ca plan.
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main.
NOVEL TRANSFER:
    Dispecerul cere cantitatea produsului 7 și nu scrie traseul prin depozit.
SPACED REVIEW:
    O zi: 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei propoziții declarative (declarative statement):
       Propoziția numește rezultatul. Optimizerul construiește planul.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainDeclarativeStatement()

Definiția unui plan de execuție (execution plan):
       Planul este al optimizerului. EXPLAIN îl arată. În tur nu a fost citit.

Exemplu:
       SqlDeclarativeLanguage_Terms.explainExecutionPlan()

🟢 Începător
    Ce este            cincizeci de întrebări despre cerere și plan
    De ce e nevoie     fără cheie, bucla trece drept plan
🟡 Producție
    Cum se scrie       SELECT data_value FROM entity_row
🔴 Staff
    Unde               rezultatul este la sfârșitul propoziției. Nu există main de recall.
Pași
    1. Recunoaște textul.
    2. Spune cine face planul.
    3. Desparte cererea de celulă.

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: În jurnal vezi SELECT data_value FROM entity_row. Cine a ales pașii?
       Capcană: Tu, pentru că ai scris FROM și coloana.
       Cheie: Optimizerul. Tu ai numit rezultatul. EXPLAIN arată indexul dacă propoziția ajunge la server. În tur nu a ajuns.
    2. Enunț: Textul este FOR EACH entity_row RETURN data_value și serverul răspunde cu eroare de sintaxă. Ce plan a rulat?
       Capcană: Planul a pornit și a căzut la primul rând.
       Cheie: Niciunul. Textul nu este o propoziție. Celulele nu au fost citite.
    3. Enunț: EXPLAIN nu a fost rulat. Ce index afirmi pentru SELECT data_value FROM entity_row?
       Capcană: PRIMARY, fiindcă orice SELECT trece prin cheie.
       Cheie: Niciunul anume. Manualul spune că EXPLAIN arată indexul. Fără citirea planului, nu îl inventa.
    4. Enunț: ANALYZE TABLE entity_row a rulat ieri. Ce i-a dat optimizerului?
       Capcană: I-a dictat pașii pe care trebuie să îi scrii tu în SELECT.
       Cheie: Statistici pentru plan. Nu schimbă textul SELECT data_value FROM entity_row și nu scrie celula. În tur ANALYZE nu a fost trimis.
    5. Enunț: O metodă Java parcurge o listă și jurnalul serverului este gol. A construit optimizerul un plan?
       Capcană: Da, pentru că ai parcurs date.
       Cheie: Nu. Fără propoziție, optimizerul nu a văzut nimic. Lista nu este entity_row.
    6. Enunț: Comentariul spune că optimizerul desparte sarcina în pași. Unde este propoziția?
       Capcană: Comentariul este planul.
       Cheie: Propoziția este SELECT-ul din namedResult. Comentariul nu ajunge la server.
    7. Enunț: Workbench arată un plan colorat. Unde este mecanismul?
       Capcană: În click-ul pe Explain.
       Cheie: În propoziția executată și în planul pe care îl construiește optimizerul. Click-ul nu este planul.
    8. Enunț: Vezi SELECT 1 AS data_value lângă SELECT data_value FROM entity_row. Care cere o celulă stocată?
       Capcană: Ambele, pentru că ambele au data_value.
       Cheie: Doar SELECT data_value FROM entity_row. Constanta nu numește tabela.
    9. Enunț: Jurnalul are textul SELECT-ului, nu și rândurile. Când vezi rezultatul?
       Capcană: Când textul intră în jurnal.
       Cheie: La sfârșitul propoziției, la client. Jurnalul nu este rezultatul și nu este planul.
    10. Enunț: Access denied apare înainte de SELECT. Ce plan s-a construit?
        Capcană: Planul a fost gata, apoi parola a oprit afișarea.
        Cheie: Niciunul. namedResult nu a fost trimis.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: După ce SELECT data_value FROM entity_row se termină, ce se schimbă în celulă?
        Capcană: data_value este scos din rând și rămâne doar la client.
        Cheie: Celula rămâne. Clientul are un rezultat. Propoziția nu este un DELETE.
    12. Enunț: Ce lacăt rămâne după ce rezultatul a plecat la client și propoziția s-a terminat?
        Capcană: Lacătul de citire rămâne până închizi Workbench-ul.
        Cheie: Din acest text nu rămâne un lacăt după ce rezultatul a plecat. Celula rămâne liberă de citit. În tur nu am măsurat un lacăt.
    13. Enunț: Un index pe data_value este scris în SELECT?
        Capcană: Da, altfel optimizerul nu are voie să îl folosească.
        Cheie: Nu este scris în propoziție. EXPLAIN spune dacă planul îl folosește. Un index nefolosit nu grăbește cererea. Nu l-am citit.
    14. Enunț: A doua sesiune citește entity_row în timp ce prima a terminat SELECT-ul. Ce a pierdut tabela?
        Capcană: Rândurile citite de prima sesiune.
        Cheie: Nimic din acel SELECT. Citirea nu consumă celula.
    15. Enunț: SELECT-ul stă într-o tranzacție și dai ROLLBACK. Ce rezultat anulezi?
        Capcană: Rândurile citite se întorc în tabelă.
        Cheie: Nu ai scris nimic de anulat. Rezultatul deja plecat la client nu este un rând șters. Celula era deja acolo.
    16. Enunț: autocommit este 1. Schimbă el momentul în care vezi data_value citit?
        Capcană: Trebuie să aștepți COMMIT ca să vezi citirea.
        Cheie: Rezultatul se vede la sfârșitul propoziției. autocommit nu transformă SELECT-ul într-o scriere.
    17. Enunț: Închizi clientul după ce a tipărit rezultatul. Ce plan rămâne pe server?
        Capcană: Planul rămâne atașat de sesiune până la următorul SELECT.
        Cheie: Planul a fost pentru propoziția aceea. Nu rămâne ca obiect în tabelă. Celula rămâne.
    18. Enunț: Când se vede efectul cererii: la ANALYZE, la EXPLAIN sau la sfârșitul SELECT-ului?
        Capcană: La ANALYZE, pentru că atunci se construiește planul.
        Cheie: Rezultatul, la sfârșitul SELECT-ului. ANALYZE actualizează statistici. EXPLAIN arată planul și nu întoarce data_value ca rezultat al cererii. Niciuna nu a fost rulată în tur.
    19. Enunț: Poate FOR EACH entity_row RETURN data_value să țină un lacăt de rând?
        Capcană: Da, pentru că numește entity_row.
        Cheie: Nu pornește, deci nu ține un lacăt. Nu este o propoziție.
    20. Enunț: rows_examined nu a fost citit. Ce poți spune despre rândurile atinse de bucla FOR EACH?
        Capcană: Le atinge pe toate, apoi raportează eroarea.
        Cheie: Pe niciunul. Serverul nu începe textul. Cifra din plan rămâne necitită și pentru SELECT, cât EXPLAIN nu a fost rulat.

Deosebire de mecanismul vecin:
    21. Enunț: De ce SELECT data_value FROM entity_row nu este celula?
        Capcană: Citește data_value, deci este data value.
        Cheie: Celula este conținutul stocat și există fără cerere. SELECT o cere. Tabela goală nu capătă un record din cerere.
    22. Enunț: De ce nu scrii tu pașii, dacă PDF-ul spune că optimizerul are abordări?
        Capcană: Abordarea se scrie în propoziție, altfel nu știe ce vrei.
        Cheie: Tu numești rezultatul. Optimizerul alege abordarea. FOR EACH îi dictează pașii și eșuează.
    23. Enunț: De ce lista DDL, DML, DCL, TCL din comentariu nu se predă aici pe rând?
        Capcană: Declarativ înseamnă toate cele patru, deci le închizi în acest folder.
        Cheie: Aici spui cine alege pașii. Care propoziție schimbă schema, rândurile, dreptul sau tranzacția se clasifică în _2_SQL_Statements.
    24. Enunț: De ce INSERT INTO entity_row (data_value) VALUES (1) nu este această cerere?
        Capcană: Spune ce vrei, deci este declarativ și gata.
        Cheie: INSERT lasă o celulă. SELECT data_value FROM entity_row o cere și nu o adaugă. Celula este folderul Introduction_To_DB.
    25. Enunț: SELECT 1 AS data_value este aceeași cerere?
        Capcană: Da, pentru că nu scrii pași.
        Cheie: Nu cere o celulă stocată: nu are FROM entity_row. Cererea de aici numește tabela și coloana.
    26. Enunț: O buclă Java peste un ResultSet este planul?
        Capcană: Da, pentru că parcurge rândurile în ordine.
        Cheie: ResultSet este rezultatul deja primit. Planul a fost pe server, pentru propoziția trimisă. Bucla din Java nu este planul.
    27. Enunț: De ce un click pe Explain nu dovedește mecanismul singur?
        Capcană: Desenul planului este propoziția.
        Cheie: Dovada este textul SELECT și planul citit. Click-ul este clientul. În tur planul nu a fost citit.
    28. Enunț: Șirul SELECT stă în Java și nu este trimis. S-a construit planul?
        Capcană: Compilarea l-a construit.
        Cheie: Nu. Optimizerul vede propoziția primită de server. Conexiunea a fost refuzată.
    29. Enunț: Când ieși către Introduction_To_DB?
        Capcană: Când SELECT are FROM, pentru că FROM este celula.
        Cheie: Când întrebarea este ce rămâne stocat. Cererea poate să nu schimbe acel conținut.
    30. Enunț: Când ieși către _2_SQL_Statements?
        Capcană: Când optimizerul alege TRUNCATE în loc de DELETE, deci ești deja acolo.
        Cheie: Când trebuie să spui ce schimbă fiecare propoziție. Faptul că optimizerul alege abordarea rămâne aici. Efectul lui TRUNCATE față de DELETE nu se predă din acest SELECT.

Eșec în producție:
    31. Enunț: Trimiți FOR EACH entity_row RETURN data_value într-o noapte de raport. Ce găsești dimineața în entity_row?
        Capcană: Jumătate din rânduri au fost citite și mutate.
        Cheie: Tabela neschimbată de acest text. Serverul nu începe. Nu există un rezultat parțial.
    32. Enunț: Un coleg spune că SELECT-ul a golit data_value, pentru că raportul a plecat. Ce verifici?
        Capcană: Raportul tipărit.
        Cheie: Dacă în entity_row celula mai este. SELECT nu o șterge. Un DELETE ar fi altă propoziție.
    33. Enunț: Ai rescris SELECT-ul în pași „ca să ajuți optimizerul”. Ce spune manualul 10.2.1?
        Capcană: Transformarea manuală este obligatorie, altfel planul este lent.
        Cheie: Evită transformările care fac interogarea greu de citit când optimizerul face el transformări. Nu am măsurat un plan în tur.
    34. Enunț: Cineva trimite comentariul „the optimizer will separate your task”. Ce plan iese?
        Capcană: Optimizerul primește comentariul și construiește planul.
        Cheie: Nu este o propoziție. Nu pornește.
    35. Enunț: Statisticile sunt vechi și SELECT-ul este lent. Ce ai schimbat dacă nu ai trimis ANALYZE TABLE?
        Capcană: SELECT-ul singur își reface statisticile la fiecare rulare.
        Cheie: Nimic la statistici. Manualul spune să rulezi ANALYZE TABLE ca optimizerul să aibă informația. În tur nu a fost trimis.
    36. Enunț: Grila rămâne deschisă și cineva o ia drept planul salvat. Ce se pierde la închiderea clientului?
        Capcană: Se pierde și planul din tabela entity_row.
        Cheie: Se pierde desenul. Planul nu este un rând în entity_row. Celula rămâne.
    37. Enunț: Adaugi USE INDEX în SELECT ca să obligi planul. Ce nu mai este doar declarativ?
        Capcană: Tot cererea, doar că mai rapidă, și indexul este garantat.
        Cheie: Ai început să dictezi planul. EXPLAIN tot trebuie citit ca să vezi ce s-a ales. În tur nu l-am citit. Celula tot nu se schimbă.
    38. Enunț: Trimiți un șir gol în loc de SELECT. Ce rând primește clientul?
        Capcană: Toate coloanele, pentru că serverul presupune cererea.
        Cheie: Niciunul din acest șir. Șirul gol nu este propoziția.
    39. Enunț: Aplicația construiește SELECT-ul și conexiunea primește Access denied. Ce plan s-a schimbat?
        Capcană: Planul a fost calculat și apoi ascuns.
        Cheie: Niciunul. Refuzul este înainte de propoziție.
    40. Enunț: Tratezi slide-ul TRUNCATE față de DELETE ca pe dovada că SELECT data_value FROM entity_row golește tabela. Ce ai amestecat?
        Capcană: Optimizerul alege, deci SELECT poate fi implementat ca TRUNCATE.
        Cheie: Slide-ul desparte două propoziții care golesc sau șterg. SELECT-ul de aici cere o coloană. Nu golește entity_row.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: Lista de cantități este doar în memoria procesului. Ce alegi?
        Capcană: O trimiți ca SELECT, ca să treacă prin optimizer.
        Cheie: O parcurgi în aplicație. Nu există tabelă de cerut. Optimizerul nu vede lista.
    42. Enunț: Cantitatea stă în entity_row și două clienți trebuie să o citească. Ce trimiți?
        Capcană: FOR EACH, ca să aibă același ordine.
        Cheie: SELECT data_value FROM entity_row. Ordinea pașilor este a optimizerului. Celula nu se mută în primul client.
    43. Enunț: Întrebarea este „ce este un record”. Unde răspunzi?
        Capcană: Cu SELECT, pentru că întoarce rânduri.
        Cheie: La Introduction_To_DB. Record-ul este rândul stocat. Rezultatul cererii nu este definiția recordului.
    44. Enunț: Întrebarea este „cine construiește planul”. Unde răspunzi?
        Capcană: La celulă, pentru că fără date nu există plan.
        Cheie: Aici. Optimizerul îl construiește pentru propoziția primită. Celula poate exista și fără planul acestei cereri.
    45. Enunț: Trebuie să spui dacă textul schimbă schema sau rândurile. De ce nu ajunge acest folder?
        Capcană: Declarativ le face pe toate, deci le și clasifici.
        Cheie: Aici spui cine alege pașii. Clasificarea este în _2_SQL_Statements.
    46. Enunț: Cineva vrea o procedură care doar face SELECT data_value FROM entity_row. Când este prea mult?
        Capcană: Procedura este singura formă declarativă.
        Cheie: SELECT-ul singur este cererea. Procedura este alt mecanism, apelat cu CALL. Nu este celula.
    47. Enunț: JDBC este în pom. Este el optimizerul?
        Capcană: Da, driverul alege planul.
        Cheie: Driverul cară propoziția. Planul îl face serverul. Un driver neconectat nu a construit un plan.
    48. Enunț: Pagina 10.2.1 vorbește despre indexuri și join-uri. Le predai aici în întregime?
        Capcană: Da, pentru că optimizerul este toate optimizările.
        Cheie: Aici rămâne faptul că tu nu scrii planul. Indexul, join-ul și WHERE-ul sunt folderele lor. Nu le închide un singur SELECT.
    49. Enunț: Îți trebuie cantitatea stocată, nu o copie de pe ecran. De ce bucla din aplicație este unealta greșită?
        Capcană: Dacă afișează 1, cantitatea este citită din stoc.
        Cheie: Bucla vede lista din proces. Stocul este celula. Ca să citești stocul, trimiți SELECT care numește tabela.
    50. Enunț: La interviu ți se cere efectul, propoziția și vecinul. Ce nu amesteci?
        Capcană: Închizi cu „SQL este declarativ” și treci la CREATE TABLE.
        Cheie: Efectul este un rezultat, nu o celulă ștearsă. Propoziția este SELECT data_value FROM entity_row. Vecinul este FOR EACH entity_row RETURN data_value, care nu pornește. Planul îl face optimizerul.
*/
// Începător: banca ține întrebările despre cerere și plan, fără un main care spune răspunsul.
// Producție: package _1_SQL._2_SQL_Declarative_Language.fast. Nu adăuga main de recall.
// Expert: Întrebările nu se lipesc pe celula din Introduction_To_DB. Vecinul lor este bucla.
package _1_SQL._2_SQL_Declarative_Language.fast;

// Începător: clasa nu trimite SELECT-ul.
// Producție: public final class SqlDeclarativeLanguage_Staff_Interview. Corpul rămâne fără main.
// Expert: Cele cincizeci stau în comentariu. Nu șterge din ele ca să rămână un alt număr.
public final class SqlDeclarativeLanguage_Staff_Interview {
    // Începător: în corp nu există o a doua propoziție.
    // Producție: acolada închide SqlDeclarativeLanguage_Staff_Interview.
    // Expert: namedResult rămâne dovada. Banca nu o înlocuiește.
}
