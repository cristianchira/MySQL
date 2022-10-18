package OPTIMIZATION;

// https://dev.mysql.com/doc/refman/5.7/en/optimization.html


/*                                           Optimization Overview



    ○ Database performance depends on several factors:


                  * Optimizing at the

                                - Database Level

                  * Optimizing at the

                                - Hardware Level

                  * Balancing

                                - Portability and Performance



                                       1. Optimizing at the Database Level



    ○ The most important factor in making a database application fast is its basic design:

                  * Are the tables structured properly?

                                - In particular, do the columns have the right data types,

                                - and does each table have the appropriate columns for the type of work?

    ○ For example, applications that perform frequent updates often have many tables

                      with few columns, while applications that analyze large amounts of data often have few tables with
                      many columns.

             * Are the right indexes

                  in place to make queries efficient?

             * Are you using the appropriate storage engine for each table,

                 and taking advantage of the strengths and features of each storage engine you use?
                 In particular, the choice of a transactional storage engine such as

                     - InnoDB or a nontransactional
                     - one such as MyISAM

             can be very important for performance and scalability.

            * Does each table use an appropriate row format?

                This choice also depends on the storage engine used for the table. In particular,

                    compressed tables

                use less disk space and so require less disk I/O to read and write the data. Compression is available for
                all kinds of workloads with InnoDB tables, and for read-only MyISAM tables.

            * Does the application use an appropriate locking strategy?

                For example, by allowing shared access when possible so that database operations can run concurrently,
                and requesting exclusive access when appropriate so that critical operations get top priority. Again,
                the choice of storage engine is significant.The InnoDB storage engine handles most locking issues without
                involvement from you, allowing for better concurrency in the database and reducing the amount of experimentation
                and tuning for your code.

            * Are all memory areas used for caching sized correctly?

                That is, large enough to hold frequently accessed data, but not so large that they overload physical memory
                and cause paging. The main memory areas to configure are the

                     - InnoDB buffer pool,
                     - the MyISAM key cache,
                     - and the MySQL_ query cache.



                                          2. Optimizing at the Hardware Level


    Any database application eventually hits hardware limits as the database becomes more and more busy. A DBA must evaluate
        whether it is possible to tune the application or reconfigure the server to avoid these bottlenecks, or whether
        more hardware resources are required.
            System bottlenecks typically arise from these sources:

    Disk seeks.

        It takes time for the disk to find a piece of data. With modern disks, the mean time for this is usually lower
        than 10ms, so we can in theory do about 100 seeks a second. This time improves slowly with new disks and is very
        hard to optimize for a single table. The way to optimize seek time is to distribute the data onto more than one disk.

    Disk reading and writing.

        When the disk is at the correct position, we need to read or write the data. With modern disks, one disk delivers
        at least 10�20MB/s throughput.This is easier to optimize than seeks because you can read in parallel from multiple disks.


    CPU cycles.

        When the data is in main memory, we must process it to get our result. Having large tables compared to the amount
        of memory is the most common limiting factor. But with small tables, speed is usually not the problem.

    Memory bandwidth.

        When the CPU needs more data than can fit in the CPU cache, main memory bandwidth becomes a bottleneck. This is
        an uncommon bottleneck for most systems, but one to be aware of.


                                           3. Balancing Portability and Performance


    To use performance-oriented SQL extensions in a portable MySQL_ program, you can wrap MySQL_-specific keywords in a
    statement within /*  *\/
 comment delimiters. Other SQL servers ignore the commented keywords. For information about writing comments, see Section 9.6, �Comments�.


                                                          NOTE



    ○ InnoDB is the default storage engine for new tables.

                   * In practice, the advanced InnoDB performance features mean that

                   * InnoDB tables often outperform the simpler MyISAM tables,

                                 - especially for a busy database.


 */






public class Optimization_ {
}
