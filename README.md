# Multi-Engine Data Service


# Plugin Configuration

`clientConnectionString` String
`databaseName` String
`clusterName` String
`stopStartupIfNoDbConnection` boolean
`pingDbOnStartup` boolean
`generateIdentityLinkInstances` boolean

## Plugin Configuration Examples:

### Spring Boot:

#### Java:

```java
class MyMongoHistoryMongoHistoryEventHandlerProcessEnginePlugin extends MongoHistoryEventHandlerProcessEnginePlugin{
    
}

```


#### Kotlin:

```kotlin
@Component
class MyMongoHistoryMongoHistoryEventHandlerProcessEnginePlugin: MongoHistoryEventHandlerProcessEnginePlugin()


```

### Camunda Distributions (Non-Springboot):

Processes.xml / bpm-platform.xml

```xml
<xml>
    
</xml>
```


# Datasource Requirements

1. MongoDB version that supports multi-document transactions (4.0+).  If you are using a sharded cluster then 4.2+ will be required.
1. MongoDB deployment with at least 3 replicas for support of the multi-document transactions.



# Relations

All History objects have a `relations` object that can hold all other history object types.



# Features:

1. Microservices Ready! Query data across multiple camunda engines and clusters. 
1. Works with Camunda's Spring Boot, Camunda Run, and other camunda supported distributions.
1. High flexible Data Importer used transfer of data from existing Camunda instances into the EngineDataService 
1. Modern DTOs for all History Events
1. DTOs for Deployments, Deployment Resources, Process Definitions, Decision Definitions, and Decision Requirements Definitions
1. History Service Query Capability
1. Configurable joins between relevant history events
1. Robust security system for all history data, allowing complex security that was not previously possible with Camunda's APIs.
1. Integration with Camunda's transactions, ensuring proper rollbacks upon error.
1. Storage of BPMN JSON Models allowing query of BPMNs as JSON structures and joins with history data.
1. Utilize Mongo's Aggregation Framework to tailor exact data query and response formation.
1. Leverage PowerBI, Tableau, Grafana, and other reporting tooling to visualize your data
1. Leverage Mongo change streams to receive realtime event changes in historic data based on Mongo Aggregation Framework queries.
1. Query Camunda Process Variables in their native formats, Text, Dates, Lists, JSON, custom objects, etc. (Query JSON!)