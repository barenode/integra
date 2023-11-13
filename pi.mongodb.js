/* global use, db */
// MongoDB Playground
// Use Ctrl+Space inside a snippet or a string literal to trigger completions.

const database = 'payment-instruction';
const collection = 'paymentInstruction';

// Create a new database.
use(database);

// db.paymentInstruction.insert({"_id":"7733684b-3937-4240-aabc-d3f38c49321e","version":{"$numberLong":"2"},"created":{"$date":{"$numberLong":"1670836315838"}},"lastModified":{"$date":{"$numberLong":"1670836320342"}},"lastActivityDate":{"$date":{"$numberLong":"1670836315838"}},"schedule":{"fromDate":{"$date":{"$numberLong":"1812855600000"}},"toDate":{"$date":{"$numberLong":"1821502800000"}},"recurrence":{"frequency":"Month","failureCount":{"$numberInt":"0"}},"nextRunDate":{"$date":{"$numberLong":"1815188400000"}},"status":"PAID"},"payer":{"billingAccountNumber":"30174046"},"receiver":{"billingAccountNumber":"30174046","serviceNumber":"420770084825"},"_class":"cz.vodafone.dxl.payment_instruction.domain.model.PaymentInstruction","status":"DEAD","type":"PAYMENT_INSTRUCTION"})


db.paymentInstruction.aggregate(
  [
    {
      $project: {
         name: 1,
         formattedDate: { $dateToString: { format: "%Y-%m-%d %H:%M", date: "$lastModified" } }
      }
    }
  ]
)

// Create a new collection.
// db.createCollection(collection);

// The prototype form to create a collection:
/* db.createCollection( <name>,
  {
    capped: <boolean>,
    autoIndexId: <boolean>,
    size: <number>,
    max: <number>,
    storageEngine: <document>,
    validator: <document>,
    validationLevel: <string>,
    validationAction: <string>,
    indexOptionDefaults: <document>,
    viewOn: <string>,
    pipeline: <pipeline>,
    collation: <document>,
    writeConcern: <document>,
    timeseries: { // Added in MongoDB 5.0
      timeField: <string>, // required for time series collections
      metaField: <string>,
      granularity: <string>,
      bucketMaxSpanSeconds: <number>, // Added in MongoDB 6.3
      bucketRoundingSeconds: <number>, // Added in MongoDB 6.3
    },
    expireAfterSeconds: <number>,
    clusteredIndex: <document>, // Added in MongoDB 5.3
  }
)*/

// More information on the `createCollection` command can be found at:
// https://www.mongodb.com/docs/manual/reference/method/db.createCollection/
