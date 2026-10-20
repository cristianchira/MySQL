# Catalog MySQL

Citește acest fișier la `update mysql`, înainte de primul `.java`. Numele de pe disc primează. Dacă un folder a apărut sau a dispărut, tabelul de acoperire din tur spune adevărul, nu acest fișier.

Nu se șterge un folder care lipsește din tabel. Tabelul se corectează. Nu se redenumește un folder ca să îndrepți ortografia (`TAble`, `GRPUP`).

## Ce se citește în tur

| Sursă | Unde stă | Cum se deschide |
|---|---|---|
| PDF-uri în rădăcina repo-ului | `*.pdf` lângă `pom.xml` | `open_books.py`, căutare în cuprins. Fără cuprins: `NO_TOC_HIT`, fără citat. |
| PDF-uri de curs, dacă apare un folder `books` | `books/` în rădăcină | doar fișierul al cărui nume se potrivește cu topicul |
| PDF-ul topicului | în folderul din `src/main/java` | fiecare PDF din folderul țintă |
| Manualul de referință | `mysql.version` din `pom.xml`, serie LTS | pagina mecanismului adusă în tur. Eșecul este `NERULAT AICI` |

Un PDF de 0 octeți este `NERULAT AICI`. Lista de titluri nu se ține minte aici: scriptul listează discul. Nu există `Luv2code`.

## Rezolvare `update mysql <nume>`

Fără cale explicită, numele lovește folderul de predare. Clasa deja scrisă în același folder este `ACOPERIT ÎN SRC`: o citești, nu o copiezi.

| Spune utilizatorul | Folder de predare |
|---|---|
| admin, administration | `_0_DB_ADMINISTRATION` |
| commands, mysql commands | `_0_DB_ADMINISTRATION/MySQL_Commands` |
| sql, introduction | `_1_SQL` |
| introduction to db | `_1_SQL/_1_Introduction_To_DB` |
| declarative | `_1_SQL/_2_SQL_Declarative_Language` |
| statements | `_2_SQL_Statements` |
| ddl | `_2_SQL_Statements/_1_DDL` |
| create | `_2_SQL_Statements/_1_DDL/_1_CREATE` |
| create table | `_2_SQL_Statements/_1_DDL/_1_CREATE/_1_Create_Table` |
| use, using database | `_2_SQL_Statements/_1_DDL/_1_CREATE/_2_Using_Database_And_Table` |
| more using table | `_2_SQL_Statements/_1_DDL/_1_CREATE/_3_More_Using_TAble` |
| alter | `_2_SQL_Statements/_1_DDL/_2_ALTER` |
| drop | `_2_SQL_Statements/_1_DDL/_3_DROP` |
| rename | `_2_SQL_Statements/_1_DDL/_4_RENAME` |
| truncate | `_2_SQL_Statements/_1_DDL/_5_TRUNCATE` |
| dml | `_2_SQL_Statements/_2_DML` |
| select | `_2_SQL_Statements/_2_DML/_1_SELECT` |
| from | `_2_SQL_Statements/_2_DML/_1_SELECT/_1_FROM` |
| where | `_2_SQL_Statements/_2_DML/_1_SELECT/_2_WHERE` |
| and | `_2_SQL_Statements/_2_DML/_1_SELECT/_3_AND` |
| or | `_2_SQL_Statements/_2_DML/_1_SELECT/_4_OR` |
| precedence | `_2_SQL_Statements/_2_DML/_1_SELECT/_5_PRECEDENCE` |
| in, not in | `_2_SQL_Statements/_2_DML/_1_SELECT/_6_IN_NOT_IN` |
| like, not like | `_2_SQL_Statements/_2_DML/_1_SELECT/_7_LIKE_NOT_LIKE` |
| wildcard | `_2_SQL_Statements/_2_DML/_1_SELECT/_8_WILDCARD` |
| between | `_2_SQL_Statements/_2_DML/_1_SELECT/_9_BETWEEN_AND` |
| is null, is not null | `_2_SQL_Statements/_2_DML/_1_SELECT/_10_IS_NOT_NULL_IS_NULL` |
| comparator, math | `_2_SQL_Statements/_2_DML/_1_SELECT/_11_MATH_COMPARATORS` |
| distinct | `_2_SQL_Statements/_2_DML/_1_SELECT/_12_DISTINCT` |
| order by | `_2_SQL_Statements/_2_DML/_1_SELECT/_13_ORDER_BY` |
| group by | `_2_SQL_Statements/_2_DML/_1_SELECT/_14_GROUP_BY` |
| having | `_2_SQL_Statements/_2_DML/_1_SELECT/_15_HAVING` |
| where or having, where vs having | `_2_SQL_Statements/_2_DML/_1_SELECT/_15_WHERE_OR_HAVING` |
| alias, aliases | `_2_SQL_Statements/_2_DML/_1_SELECT/_16_ALIASES` |
| limit | `_2_SQL_Statements/_2_DML/_1_SELECT/_17_LIMIT` |
| insert | `_2_SQL_Statements/_2_DML/_2_INSERT` |
| values | `_2_SQL_Statements/_2_DML/_2_INSERT/_1_VALUES` |
| insert select, from another table | `_2_SQL_Statements/_2_DML/_2_INSERT/_2_FROM_ANOTHER_TABLE` |
| delete | `_2_SQL_Statements/_2_DML/_3_DELETE` |
| drop truncate delete | `_2_SQL_Statements/_2_DML/_3_DROP_TRUNCATE_DELETE` |
| update statement, sql update | `_2_SQL_Statements/_2_DML/_4_UPDATE` |
| call | `_2_SQL_Statements/_2_DML/_5_CALL` |
| subquery, subqueries | `_2_SQL_Statements/_2_DML/_6_SUBQUERIES` |
| exists, not exists | `_2_SQL_Statements/_2_DML/_6_SUBQUERIES/EXISTS_NOT_EXISTS` |
| complex subquery | `_2_SQL_Statements/_2_DML/_6_SUBQUERIES/COMPLEX` |
| dcl | `_2_SQL_Statements/_3_DCL` |
| grant | `_2_SQL_Statements/_3_DCL/GRANT` |
| revoke | `_2_SQL_Statements/_3_DCL/REVOKE` |
| tcl | `_2_SQL_Statements/_4_TCL` |
| commit | `_2_SQL_Statements/_4_TCL/COMMIT` |
| rollback | `_2_SQL_Statements/_4_TCL/ROLLBACK` |
| savepoint | `_2_SQL_Statements/_4_TCL/SAVEPOINT` |
| keywords | `_3_SQL_Keywords` |
| terminology | `_4_Database_Terminology` |
| relational, essentials | `_4_Database_Terminology/_1_Relational_DB_Essentials` |
| acid | `_4_Database_Terminology/_2_ACID` |
| warehouse, data warehouse | `_4_Database_Terminology/Data_Warehouse` |
| dbms | `_4_Database_Terminology/DBMS` |
| innodb | `_4_Database_Terminology/InnoDB` |
| normalized, normalization | `_4_Database_Terminology/Normalized` |
| oltp | `_4_Database_Terminology/OLTP` |
| row lock, lock | `_4_Database_Terminology/Row_Lock` |
| storage engine, engine | `_4_Database_Terminology/Storage_Engine` |
| transaction | `_4_Database_Terminology/Transaction` |
| schema | `_5_Relational_Schema` |
| foreign key schema | `_5_Relational_Schema/_2_Foreign_Key` |
| unique schema | `_5_Relational_Schema/_3_Unique_Key` |
| regular index | `_5_Relational_Schema/_4_Regular_Index` |
| relationship | `_5_Relational_Schema/_5_Relationship` |
| workbench, client server | `_6_Client_Server_Workbench` |
| connection | `_6_Client_Server_Workbench/_1_Setting_Up_A_Connection` |
| user, create user | `_6_Client_Server_Workbench/_2_Creating_A_New_User` |
| database, create database | `_6_Client_Server_Workbench/_3_Creating_A_Database` |
| types, data types | `_7_Data_Types` |
| default value | `_7_Data_Types/_0_Data_Types_Default_Values` |
| numeric | `_7_Data_Types/_1_Numeric` |
| integer, int | `_7_Data_Types/_1_Numeric/_1_Integers` |
| fixed, decimal | `_7_Data_Types/_1_Numeric/Fixed_Point` |
| float, floating | `_7_Data_Types/_1_Numeric/Floating_Point` |
| date, datetime, timestamp | `_7_Data_Types/_2_Date_DateTime_Timestamp` |
| string | `_7_Data_Types/_3_String_Data_Type` |
| string syntax | `_7_Data_Types/_3_String_Data_Type/_1_String_Data_Type_Syntax` |
| char, varchar | `_7_Data_Types/_3_String_Data_Type/_2_CHAR_AND_VARCHAR` |
| binary, varbinary | `_7_Data_Types/_3_String_Data_Type/_3_BINARY_and_VARBINARY` |
| blob, text | `_7_Data_Types/_3_String_Data_Type/_4_BLOB_And_TEXT` |
| enum | `_7_Data_Types/_3_String_Data_Type/_5_ENUM` |
| set type | `_7_Data_Types/_3_String_Data_Type/_6_SET_TYPE` |
| spatial | `_7_Data_Types/_4_Spatial` |
| json | `_7_Data_Types/_5_JSON` |
| constraints | `_8_Constraints` |
| autoincrement, auto increment | `_8_Constraints/_0_Autoincrement` |
| primary key, primary | `_8_Constraints/_1_Primary_Key` |
| foreign key, fk | `_8_Constraints/_2_Foreign_Key` |
| index, indexes | `_8_Constraints/_3_Indexes` |
| unique, unique key | `_8_Constraints/_4_Unique_Key` |
| not null | `_8_Constraints/Not_Null` |
| default constraint | `_8_Constraints/Default_Constraint` |
| coding technique, best practice sql | `_9_Coding_Technique` |
| joins, join | `_10_JOINs` |
| inner, inner join | `_10_JOINs/_1_INNER_JOIN` |
| duplicate | `_10_JOINs/_2_DUPLICATE_RECORDS` |
| left, left join | `_10_JOINs/_3_LEFT_JOIN` |
| cross, cross join | `_10_JOINs/_4_CROSS_JOIN` |
| union, union all | `_10_JOINs/_5_UNION_VS_UNION_ALL` |
| self join | `_10_JOINs/SELF_JOIN` |
| multiple join | `_10_JOINs/JOIN_MULTIPLE_TABLE` |
| join aggregate | `_10_JOINs/JOIN_AGGREGATE_FUNCTION` |
| join tricks | `_10_JOINs/JOIN_TRICKS` |
| routines, stored routines | `_11_STORED_ROUTINES` |
| procedure, stored procedure | `_11_STORED_ROUTINES/STORED_PROCEDURES` |
| input parameter | `_11_STORED_ROUTINES/STORED_PROCEDURES/INPUT_PARAMETER` |
| output parameter | `_11_STORED_ROUTINES/STORED_PROCEDURES/OUTPUT_PARAMETER` |
| inout, input output | `_11_STORED_ROUTINES/STORED_PROCEDURES/INPUT_OUTPUT_PARAMETER` |
| no parameter, non parametric | `_11_STORED_ROUTINES/STORED_PROCEDURES/NON_PARAMETRIC` |
| function, functions | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS` |
| aggregate | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION` |
| count | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/COUNT` |
| sum | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/SUM` |
| avg | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/AVG` |
| min, max | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/MIN_MAX` |
| round | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/ROUND` |
| coalesce, ifnull | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/COALESCE` |
| operator, operators | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/OPERATORS` |
| udf, user defined function | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/USER_DEFINED_FUNCTION` |
| built in | `_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/BUILT_IN` |
| optimization | `_12_DB_Optimization` |
| index optimization | `_12_DB_Optimization/INDEX_OPTIMIZATION` |
| statement optimization | `_12_DB_Optimization/SQL_STATEMENTS_OPTIMIZATION_` |
| security | `_13_MySQL_Security` |
| view, views | `_14_VIEW` |
| triggers, trigger | `_15_TRIGGERS` |
| before | `_15_TRIGGERS/BEFORE` |
| after | `_15_TRIGGERS/AFTER` |
| variables | `_16_SQL_Variables` |
| local variable | `_16_SQL_Variables/LOCAL_VARIABLES` |
| session variable | `_16_SQL_Variables/SESSION_VARIABLES` |
| global variable | `_16_SQL_Variables/GLOBAL_VARIABLES` |
| user variable, system variable | `_16_SQL_Variables/USER_DEFINED_VS_SYSTEM_VARIABLES` |

Căile sunt sub `src/main/java/`. Numele de clasă păstrează ortografia existentă. Nu redenumi clasa ca să îndrepți un cuvânt. Folderul de pachet al lecției scurte este `fast`.

`_3_SQL_Keywords` și `_9_Coding_Technique` nu sunt un mecanism de server. `fast` de acolo predă cum alegi propoziția sau cum citești o convenție. Nu copia acolo banca unui topic.

`update statement` și `sql update` lovesc `_4_UPDATE`. `update` fără `mysql` nu intră în acest catalog.

## Familii

| Familie | Copii | `fast` predă |
|---|---|---|
| Statements | `_2_SQL_Statements` și copiii, `_3_SQL_Keywords` | ce propoziție schimbă schema, rândurile, dreptul sau tranzacția |
| Schema | `_5_Relational_Schema`, `_8_Constraints`, `_14_VIEW` | cheie, constrângere sau view |
| Server | `_0_DB_ADMINISTRATION`, `_4_Database_Terminology`, `_6_Client_Server_Workbench` | cine execută și pe ce motor |
| Types | `_7_Data_Types` și copiii | ce tip păstrează valoarea și ce pierde |
| Joins | `_10_JOINs` și copiii, `_2_SQL_Statements/_2_DML/_6_SUBQUERIES` | cum legi rândurile și când subcererea nu este un join |
| Routines | `_11_STORED_ROUTINES`, `_15_TRIGGERS`, `_16_SQL_Variables` | procedură, funcție, trigger, domeniul variabilei |
| Security | `_13_MySQL_Security`, `_2_SQL_Statements/_3_DCL` | cine poate rula propoziția |
| Transaction | `_2_SQL_Statements/_4_TCL`, `Transaction`, `_2_ACID`, `Row_Lock` | când schimbarea rămâne și ce se ține |
| Optimization | `_12_DB_Optimization`, `_4_Regular_Index`, `_3_Indexes`, `_9_Coding_Technique` | planul, nu propoziția rescrisă din obișnuință |

`server` și `query` nu sunt foldere. `update mysql` pe ele scrie inventarul familiei (`Server`, respectiv `Joins` împreună cu clauzele din `SELECT`) și închide un singur copil care nu este `VERIFIED`. `statements`, `joins`, `constraints`, `types`, `routines`, `security`, `optimization` rămân folderele din tabelul de rezolvare. `transaction` este folderul `_4_Database_Terminology/Transaction`, nu familia.

## Perechi de interviu

În fișier intră perechea mecanismului, nu toată lista. Cheia spune condiția care le separă. Statusul unui mecanism scos se citește din manualul versiunii din pom, nu din acest tabel.

| Mecanism | Se confundă cu | Îi separă |
|---|---|---|
| DDL | DML | DDL schimbă schema. DML schimbă rânduri. |
| DELETE | TRUNCATE | DELETE șterge rânduri, poate avea WHERE și, în InnoDB, se poate da înapoi. TRUNCATE golește tabela. |
| TRUNCATE | DROP | TRUNCATE lasă definiția. DROP scoate obiectul. |
| UPDATE | INSERT | UPDATE schimbă rânduri care există. INSERT adaugă rânduri. |
| WHERE | HAVING | WHERE filtrează rândurile înainte de grup. HAVING filtrează grupurile. |
| AND | OR | AND cere toate condițiile. OR acceptă una. AND se leagă înaintea lui OR. |
| IN | EXISTS | IN compară o valoare cu o listă. EXISTS întreabă dacă subcererea întoarce un rând. NULL în listă schimbă IN. |
| LIKE | = | LIKE folosește tiparul. În =, `%` este caracter. |
| BETWEEN | două comparații | BETWEEN este inclusiv la ambele capete. |
| IS NULL | = NULL | = NULL nu este adevărat în logica cu trei valori. IS NULL este testul. |
| DISTINCT | GROUP BY | DISTINCT elimină rândurile duplicate din rezultat. GROUP BY face grupuri ca să poți agrega. |
| COUNT(*) | COUNT(coloană) | COUNT(*) numără rândurile. COUNT(coloană) sare peste NULL pe coloana aceea. |
| INNER JOIN | LEFT JOIN | INNER păstrează doar potrivirile. LEFT păstrează fiecare rând din stânga; dreapta lipsă este NULL. |
| LEFT JOIN | RIGHT JOIN | Aceeași potrivire. Se schimbă partea păstrată. |
| CROSS JOIN | INNER fără condiție | CROSS este produsul scris. Un JOIN fără ON nu este un CROSS declarat. |
| UNION | UNION ALL | UNION elimină duplicatele. UNION ALL le păstrează. |
| Subquery | JOIN | Subcererea este o interogare în alta. JOIN este o potrivire în același FROM. Același rezultat nu este același plan. |
| CHAR | VARCHAR | CHAR ocupă lungimea declarată. VARCHAR ține lungimea folosită. |
| DATETIME | TIMESTAMP | DATETIME este valoarea de calendar. TIMESTAMP se convertește după time_zone. |
| FLOAT | DECIMAL | FLOAT este aproximativ. DECIMAL ține scara exactă. |
| BLOB | TEXT | BLOB este octeți. TEXT este caractere. |
| ENUM | VARCHAR | ENUM limitează lista. VARCHAR nu refuză valoarea din afara listei. |
| PRIMARY KEY | UNIQUE | PRIMARY KEY este una și nu acceptă NULL. UNIQUE poate apărea de mai multe ori; în InnoDB acceptă mai multe NULL. |
| PRIMARY KEY | index secundar | În InnoDB, PRIMARY KEY este indexul grupat. Indexul secundar ține cheia primară. |
| FOREIGN KEY | coloană simplă | FOREIGN KEY refuză copilul fără părinte, după acțiunea scrisă. Coloana fără constrângere nu verifică. |
| NOT NULL | DEFAULT | NOT NULL refuză lipsa. DEFAULT umple lipsa când INSERT nu dă coloana. |
| AUTO_INCREMENT | PRIMARY KEY | AUTO_INCREMENT generează numărul. PRIMARY KEY este constrângerea. Pot sta împreună. |
| InnoDB | MyISAM | InnoDB ține tranzacția și lacătul de înregistrare. MyISAM nu este motorul implicit și nu ține tranzacția. |
| Row lock | table lock | Lacătul de rând ține înregistrarea din index. Fără index pe filtrul din WHERE, InnoDB poate ține mai mult decât rândul numit. |
| COMMIT | ROLLBACK | COMMIT face schimbarea durabilă. ROLLBACK o anulează. |
| SAVEPOINT | ROLLBACK | SAVEPOINT pune un semn. ROLLBACK fără TO anulează toată tranzacția. |
| ACID | autocommit | ACID este proprietatea tranzacției. autocommit=1 face fiecare propoziție o tranzacție. |
| Procedură | funcție | Procedura se apelează cu CALL. Funcția întoarce o valoare și intră într-o expresie. |
| BEFORE | AFTER | BEFORE vede rândul înainte să fie scris. AFTER îl vede după. |
| Trigger | constrângere | Trigger-ul rulează cod. Constrângerea refuză rândul. Trigger-ul nu ține loc de FOREIGN KEY. |
| View | tabelă | View-ul este o interogare salvată. Tabela ține rândurile. |
| Variabilă locală | variabilă utilizator | DECLARE trăiește în rutină. @variabila trăiește pe sesiune. |
| Variabilă de sesiune | variabilă globală | SET SESSION ține conexiunea. SET GLOBAL nu schimbă valoarea deja copiată în sesiunea curentă. |
| GRANT | CREATE USER | CREATE USER face contul. GRANT dă dreptul. Un cont fără privilegiu se poate autentifica și nu poate citi tabela. |
| Index | citirea tabelei | Indexul scurtează căutarea când planul îl folosește. Un index nefolosit nu grăbește propoziția. |
| EXPLAIN | propoziția | EXPLAIN arată planul. Nu schimbă rândurile. |
| Normalizare | tabelă lată | Forma normală ține un fapt o dată. Tabela lată repetă valoarea. |
| OLTP | depozit de date | OLTP ține tranzacția scurtă pe rânduri curente. Depozitul ține citiri mari pe istoric. |

## Ce dovedește expertul la interviu

Pe mecanismul întrebat, în două minute, fără notițe:

1. Efectul pe rânduri, schemă, index sau tranzacție, fără a repeta numele ca definiție.
2. Momentul în care efectul se vede: sfârșitul propoziției, commit, sau propoziția următoare.
3. O consecință pe care o plătești: lacăt, NULL, plan sau drept.
4. Mecanismul vecin și condiția care le separă.
5. Un semn din SQL sau din `EXPLAIN` că mecanismul este folosit greșit.

Recunoașterea demo-ului din repo nu închide `EXPERT`. Transferul schimbă domeniul.
