mesomatic: mesos interaction in idiomatic clojure
=================================================

Mesomatic provides facilities to interact with [Apache Mesos](http://mesos.apache.org)
from clojure. It is essentially a simple facade around the Mesos JAVA API.

Mesomatic versions match the API version they target.

## Usage

Add this to your leiningen profile.

```clojure
:dependencies [org.spootnik/mesomatic "0.21.1"]
```

## Namespaces

- `mesomatic.types` contains a facade to and from all protobuf types.

## Type conversions

To go to and from protobuf types, mesomatic uses two simple functions:

- `pb->data`: yields a data structure from a mesos type, usually in the form of
              a record.
- `data->pb`: converts a data structure to a mesos type.

By yielding records, mesomatic provides elements which are homomorphic to
maps and can easily be converted back to protobuf.

#### Special cases

A few cases do not yield records:

- Scalar values (`Protos.Value.Scalar`) yield doubles.
- All enums yield keywords.
- Set values (`Protos.Value.Set`) yield sets.
- Some types containing a single repeated field are unrolled
  as a seq of their content, such as `Protos.Value.Ranges`.

#### Supported Types

- Status (as keywords)
- FrameworkID
- OfferID
- SlaveID
- TaskID
- ExecutorID
- ContainerID
- FrameworkInfo
- HealthCheck.HTTP (as HealthCheckHTTP)
- HealthCheck
- CommandInfo
- CommandInfo.URI (as URI)
- CommandInfo.Container (as CommandInfoContainer)
- ExecutorInfo
- MasterInfo
- SlaveInfo
- Value
- Value.Type (as keywords)
- Value.Range (as ValueRange)
- Value.Ranges (yields a seq of ValueRange)
- Value.Set (as Clojure sets)
- Value.Text (as strings)
- Attribute
- Resource
- ResourceStatistics
- ResourceUsage
- PerfStatistics
- Request
- Offer
- TaskInfo
- TaskState
- TaskStatus
- TaskStatus.Source (as keywords)
- TaskStatus.Reason (as keywords)
- Filters
- Environment
- Environment.Variable (as EnvironmentVariable)
- Parameter
- Parameters
- Credential
- Credentials
- ACL
- ACL.Entity (as ACLEntity)
- ACL.Entity.Type (as keywords)
- ACL.RegisterFramework (as ACLRegisterFramework)
- ACL.RunTask (as ACLRunTask)
- ACL.ShutdownFramework (as ACLShutdownFramework)
- ACLs
- RateLimit
- RateLimits
- Volume
- Volume.Mode (as keywords)
- ContainerInfo
- ContainerInfo.Type (as keywords)
- ContainerInfo.DockerInfo (as DockerInfo)
- ContainerInfo.DockerInfo.PortMapping (as PortMapping)


