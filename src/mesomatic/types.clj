(ns org.spootnik.mesos-data
  "Utility functions to convert to and from mesos types."
  (:import org.apache.mesos.Protos$Status
           org.apache.mesos.Protos$FrameworkID
           org.apache.mesos.Protos$OfferID
           org.apache.mesos.Protos$SlaveID
           org.apache.mesos.Protos$TaskID
           org.apache.mesos.Protos$ExecutorID
           org.apache.mesos.Protos$ContainerID
           org.apache.mesos.Protos$FrameworkInfo
           org.apache.mesos.Protos$HealthCheck
           org.apache.mesos.Protos$HealthCheck$HTTP
           org.apache.mesos.Protos$CommandInfo
           org.apache.mesos.Protos$CommandInfo$URI
           org.apache.mesos.Protos$CommandInfo$ContainerInfo
           org.apache.mesos.Protos$ExecutorInfo
           org.apache.mesos.Protos$MasterInfo
           org.apache.mesos.Protos$SlaveInfo
           org.apache.mesos.Protos$Value
           org.apache.mesos.Protos$Value$Type
           org.apache.mesos.Protos$Value$Scalar
           org.apache.mesos.Protos$Value$Range
           org.apache.mesos.Protos$Value$Ranges
           org.apache.mesos.Protos$Value$Set
           org.apache.mesos.Protos$Value$Text
           org.apache.mesos.Protos$Attribute
           org.apache.mesos.Protos$Resource
           org.apache.mesos.Protos$ResourceStatistics
           org.apache.mesos.Protos$ResourceUsage
           org.apache.mesos.Protos$PerfStatistics
           org.apache.mesos.Protos$Request
           org.apache.mesos.Protos$Offer
           org.apache.mesos.Protos$TaskInfo
           org.apache.mesos.Protos$TaskState
           org.apache.mesos.Protos$TaskStatus
           org.apache.mesos.Protos$TaskStatus$Source
           org.apache.mesos.Protos$TaskStatus$Reason
           org.apache.mesos.Protos$Filters
           org.apache.mesos.Protos$Environment
           org.apache.mesos.Protos$Environment$Variable
           org.apache.mesos.Protos$Parameter
           org.apache.mesos.Protos$Parameters
           org.apache.mesos.Protos$Credential
           org.apache.mesos.Protos$Credentials
           org.apache.mesos.Protos$ACL
           org.apache.mesos.Protos$ACL$Entity
           org.apache.mesos.Protos$ACL$Entity$Type
           org.apache.mesos.Protos$ACL$RegisterFramework
           org.apache.mesos.Protos$ACL$RunTask
           org.apache.mesos.Protos$ACL$ShutdownFramework
           org.apache.mesos.Protos$ACLs
           org.apache.mesos.Protos$RateLimit
           org.apache.mesos.Protos$RateLimits
           org.apache.mesos.Protos$Volume
           org.apache.mesos.Protos$Volume$Mode
           org.apache.mesos.Protos$ContainerInfo
           org.apache.mesos.Protos$ContainerInfo$Type
           org.apache.mesos.Protos$ContainerInfo$DockerInfo
           org.apache.mesos.Protos$ContainerInfo$DockerInfo$Network
           org.apache.mesos.Protos$ContainerInfo$DockerInfo$PortMapping))

;; Our two exported signatures: data->pb and pb->data

(defprotocol Serializable
  "Interface to convert from clojure data to mesos protobuf
   payloads."
  (data->pb [this]))

(defmulti pb->data
  "Open protocol to convert from mesos protobuf to clojure"
  class)


;; Status
;; ======
;;
;; Enums are a bit of a special case since we don't wrap
;; them in a record, instead just yield a keyword which
;; will can be converted back with the extend-protocol
;; trick later on.

(defmethod pb->data Protos$Status
  [^Protos$Status status]
  (case status
    Protos$Status/DRIVER_NOT_STARTED :driver-not-started
    Protos$Status/DRIVER_RUNNING     :driver-running
    Protos$Status/DRIVER_ABORTED     :driver-aborted
    Protos$Status/DRIVER_STOPPED     :driver-stopped))

;; FrameworkID
;; ===========
;;
;; All payloads will follow this idiom of defining
;; a record which is serializable and have deserialization
;; yield an instance of that protocol.
(defrecord FrameworkID [value]
  Serializable
  (data->pb [this]
    (-> (Protos$FrameworkID/newBuilder)
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$FrameworkID
  [^Protos$FrameworkID id]
  (FrameworkID. (.getValue id)))

;; OfferID
;; =======

(defrecord OfferID [value]
  Serializable
  (data->pb [this]
    (-> (Protos$OfferID/newBuilder)
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$OfferID
  [^Protos$OfferID id]
  (OfferID. (.getValue id)))

;; SlaveID
;; =======

(defrecord SlaveID [value]
  Serializable
  (data->pb [this]
    (-> (Protos$SlaveID/newBuilder)
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$SlaveID
  [^Protos$SlaveID id]
  (SlaveID. (.getValue id)))

;; TaskID
;; ======

(defrecord TaskID [value]
  Serializable
  (data->pb [this]
    (-> (Protos$TaskID/newBuilder)
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$TaskID
  [^Protos$TaskID id]
  (TaskID. (.getValue id)))

;; ExecutorID
;; ==========

(defrecord ExecutorID [value]
  Serializable
  (data->pb [this]
    (-> (Protos$ExecutorID/newBuilder)
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$ExecutorID
  [^Protos$ExecutorID id]
  (ExecutorID. (.getValue id)))

;; ContainerID
;; ===========

(defrecord ContainerID [value]
  Serializable
  (data->pb [this]
    (-> (Protos$ContainerID/newBuilder)
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$ContainerID
  [^Protos$ContainerID id]
  (ContainerID. (.getValue id)))

;; FrameworkInfo
;; =============


(defrecord FrameworkInfo [user name id failover-timeout checkpoint role
                          hostname principal webui-url]
  Serializable
  (data->pb [this]
    (-> (Protos$FrameworkInfo/newBuilder)
        (.setUser (str (or name "")))
        (.setName (str name))
        (cond->
            id               (.setId (data->pb id))
            failover-timeout (.setFailoverTimeout (double failover-timeout))
            checkpoint       (.setCheckpoint (boolean checkpoint))
            role             (.setRole (str role))
            hostname         (.setHostname (str hostname))
            principal        (.setPrincipal (str principal))
            webui-url        (.setWebuiUrl (str webui-url)))
        (.build))))

(defmethod pb->data Protos$FrameworkInfo
  [^Protos$FrameworkInfo info]
  (FrameworkInfo.
   (.getUser info)
   (.getName info)
   (when-let [id (.getId info)] (pb->data id))
   (.getFailoverTimeout info)
   (.getCheckpoint info)
   (.getRole info)
   (.getHostname info)
   (.getPrincipal info)
   (.getWebuiUrl info)))

;; HealthCheck
;; ===========

(defrecord HealthCheckHTTP [port path statuses]
  Serializable
  (data->pb [this]
    (let [add-statuses (fn [builder statuses]
                         (doseq [status statuses]
                           (.addStatuses builder (int status)))
                         builder)]
      (-> (Protos$HealthCheck$HTTP/newBuilder)
          (.setPort (int port))
          (cond-> path (.setPath (str path)))
          (add-statuses statuses)
          (.build)))))

(defmethod pb->data Protos$HealthCheck$HTTP
  [^Protos$HealthCheck$HTTP http]
  (HealthCheckHTTP.
   (.getPort http)
   (.getPath http)
   (.getStatusesList http)))

(defrecord HealthCheck [http delay-seconds interval-seconds
                        timeout-seconds consecutive-failures
                        grace-period-seconds command]
  Serializable
  (data->pb [this]
    (-> (Protos$HealthCheck/newBuilder)
        (cond->
            http                 (.setHttp (data->pb http))
            delay-seconds        (.setDelaySeconds (double delay-seconds))
            interval-seconds     (.setIntervalSeconds (double interval-seconds))
            timeout-seconds      (.setTimeoutSeconds (double timeout-seconds))
            consecutive-failures (.setConsecutiveFailures
                                  (int consecutive-failures))
            grace-period-seconds (.setGracePeriodSeconds
                                  (double grace-period-seconds))
            command              (.setCommand (data->pb command)))
        (.build))))

(defmethod pb->data Protos$HealthCheck
  [^Protos$HealthCheck check]
  (HealthCheck.
   (when-let [http (.getHttp check)] (pb->data http))
   (.getDelaySeconds check)
   (.getIntervalSeconds check)
   (.getTimeoutSeconds check)
   (.getConsecutiveFailures check)
   (.getGracePeriodSeconds check)
   (when-let [cmd (.getCommand check)] (pb->data cmd))))

;; CommandInfo
;; ===========

(defrecord URI [value executable extract]
  Serializable
  (data->pb [this]
    (-> (Protos$CommandInfo$URI/newBuilder)
        (.setValue (str value))
        (cond->
            executable (.setExecutable (boolean executable))
            extract    (.setExtract (boolean extract)))
        (.build))))

(defmethod pb->data Protos$CommandInfo$URI
  [^Protos$CommandInfo$URI uri]
  (URI. (.getValue uri) (.getExecutable uri) (.getExtract uri)))

(defrecord CommandInfoContainer [image options]
  Serializable
  (data->pb [this]
    (let [add-options (fn [builder options]
                        (doseq [^String opt options]
                          (.addOptions builder (str opt)))
                        builder)]
      (-> (Protos$CommandInfo$ContainerInfo/newBuilder)
          (.setImage (str image))
          (add-options options)
          (.build)))))

(defmethod pb->data Protos$CommandInfo$ContainerInfo
  [^Protos$CommandInfo$ContainerInfo container]
  (CommandInfoContainer.
   (.getImage container)
   (.getOptionsList container)))

(defrecord CommandInfo [container uris environment
                        shell value arguments user]
  Serializable
  (data->pb [this]
    (let [add-uris      (fn [builder uris]
                          (doseq [^URI uri uris]
                            (.addUris builder (data->pb uri)))
                          builder)
          add-arguments (fn [builder args]
                          (doseq [^String arg args]
                            (.addArguments builder (str arg)))
                          builder)]
      (-> (Protos$CommandInfo/newBuilder)
          (cond->
              container   (.setContainer (data->pb container))
              environment (.setEnvironment (data->pb environment))
              shell       (.setShell (boolean shell))
              value       (.setValue (str value))
              user        (.setUser (str user)))
          (add-uris uris)
          (add-arguments arguments)
          (.build)))))

(defmethod pb->data Protos$CommandInfo
  [^Protos$CommandInfo info]
  (CommandInfo.
   (when-let [cnt (.getContainer info)] (pb->data cnt))
   (map pb->data (.getUrisList info))
   (when-let [env (.getEnvironment info)] (pb->data env))
   (.getShell info)
   (.getValue info)
   (.getArgumentsList info)
   (.getUser info)))

;; ExecutorInfo
;; ============

(defrecord ExecutorInfo [executor-id framework-id command container
                         resources name source data discovery]
  Serializable
  (data->pb [this]
    (let [add-resources (fn [builder resources]
                          (doseq [r resources]
                            (.addResources builder (data->pb r)))
                          builder)]
      (-> (Protos$ExecutorInfo/newBuilder)
          (.setExecutorId (data->pb executor-id))
          (.setCommandInfo (data->pb command))
          (cond->
              framework-id (.setFrameWorkId (data->pb framework-id))
              container    (.setContainer (data->pb container))
              name         (.setName (str name))
              source       (.setSource (str source))
              data         (.setData data)
              discovery    (.setDiscovery (data->pb discovery)))
          (add-resources resources)
          (.build)))))

(defmethod pb->data Protos$ExecutorInfo
  [^Protos$ExecutorInfo info]
  (ExecutorInfo.
   (pb->data (.getExecutorId info))
   (when-let [id (.getFrameworkId info)] (pb->data id))
   (pb->data (.getCommand info))
   (when-let [cnt (.getContainer info)] (pb->data cnt))
   (map pb->data (.getResourcesList info))
   (.getName info)
   (.getSource info)
   (.getData info)
   (when-let [disco (.getDiscovery info)] (pb->data disco))))

;; MasterInfo
;; ==========

(defrecord MasterInfo [id ip port pid hostname]
  Serializable
  (data->pb [this]
    (-> (Protos$MasterInfo/newBuilder)
        (.setId (str id))
        (.setIp (int ip))
        (.setPort (int port))
        (cond->
            pid      (.setPid (str pid))
            hostname (.setHostname (str hostname)))
        (.build))))

(defmethod pb->data Protos$MasterInfo
  [^Protos$MasterInfo info]
  (MasterInfo.
   (.getId info)
   (.getIp info)
   (.getPort info)
   (.getPid info)
   (.getHostname info)))

;; SlaveInfo
;; =========

(defrecord SlaveInfo [hostname port resources attributes id checkpoint]
  Serializable
  (data->pb [this]
    (let [add-resources  (fn [builder resources]
                           (doseq [r resources]
                             (.addResources builder (data->pb r)))
                           builder)
          add-attributes (fn [builder attributes]
                           (doseq [attr attributes]
                             (.addAttributes builder (data->pb attr)))
                           builder)]
      (-> (Protos$SlaveInfo/newBuilder)
          (.setHostname (str hostname))
          (cond->
              port       (.setPort (int port))
              id         (.setId (data->pb id))
              checkpoint (.setCheckPoint (boolean checkpoint)))
          (add-resources resources)
          (add-attributes attributes)
          (.build)))))

(defmethod pb->data Protos$SlaveInfo
  [^Protos$SlaveInfo info]
  (SlaveInfo.
   (.getHostname info)
   (.getPort info)
   (map pb->data (.getResourcesList info))
   (map pb->data (.getAttributes info))
   (when-let [id (.getId info)] (pb->data id))
   (.getCheckpoint info)))

;; Value
;; =====

(defmethod pb->data Protos$Value$Type
  [^Protos$Value$Type type]
  (case type
    Protos$Value$Type/SCALAR :value-scalar
    Protos$Value$Type/RANGES :value-ranges
    Protos$Value$Type/SET    :value-set
    Protos$Value$Type/TEXT   :value-text))

(defmethod pb->data Protos$Value$Scalar
  [^Protos$Value$Scalar scalar]
  (.getValue scalar))

(defrecord ValueRange [begin end]
  Serializable
  (data->pb [this]
    (-> (Protos$Value$Range/newBuilder)
        (.setBegin (long begin))
        (.setEnd (long end))
        (.build))))

(defmethod pb->data Protos$Value$Range
  [^Protos$Value$Range range]
  (ValueRange. (.getBegin range) (.getEnd range)))

(defrecord ValueRanges [ranges]
  Serializable
  (data->pb [this]
    (let [builder (Protos$Value$Ranges/newBuilder)]
      (doseq [range ranges]
        (.addRange builder range))
      (.build builder))))

(defmethod pb->data Protos$Value$Ranges
  [^Protos$Value$Ranges ranges]
  (ValueRanges. (.getRangeList ranges)))

(defmethod pb->data Protos$Value$Set
  [^Protos$Value$Set value-set]
  (set (.getItemList value-set)))

(defmethod pb->data Protos$Value$Text
  [^Protos$Value$Text x]
  (.getValue x))

(defrecord Value [type scalar ranges set text]
  Serializable
  (data->pb [this]
    (-> (Protos$Value/newBuilder)
        (.setType (data->pb type))
        (cond->
            scalar (.setScalar (data->pb scalar))
            ranges (.setRanges (data->pb (ValueRanges. ranges)))
            set    (.setSet (data->pb set))
            text   (.setText (data->pb text)))
        (.build))))

(defmethod pb->data Protos$Value
  [^Protos$Value v]
  (Value.
   (.getType v)
   (.getScalar v)
   (when-let [ranges (.getRanges v)]
     (map pb->data (.getRangeList ranges)))
   (when-let [s (.getSet v)] (pb->data s))
   (when-let [t (.getText v)] (pb->data t))))

;; Attribute
;; =========

(defrecord Attribute [name type scalar ranges set text]
  Serializable
  (data->pb [this]
    (-> (Protos$Attribute/newBuilder)
        (.setName name)
        (.setType (data->pb type))
        (cond->
            scalar (.setScalar (data->pb scalar))
            ranges (.setRanges (data->pb (ValueRanges. ranges)))
            set    (.setSet (data->pb set))
            text   (.setText (data->pb text)))
        (.build))))

(defmethod pb->data Protos$Attribute
  [^Protos$Attribute v]
  (Attribute.
   (.getName v)
   (.getType v)
   (.getScalar v)
   (when-let [ranges (.getRanges v)]
     (map pb->data (.getRangeList ranges)))
   (when-let [s (.getSet v)] (pb->data s))
   (when-let [t (.getText v)] (pb->data t))))

;; Resource
;; ========

(defrecord Resource [name type scalar ranges set role]
  Serializable
  (data->pb [this]
    (-> (Protos$Resource/newBuilder)
        (.setName name)
        (.setType (data->pb type))
        (cond->
            scalar (.setScalar (data->pb scalar))
            ranges (.setRanges (data->pb (ValueRanges. ranges)))
            set    (.setSet (data->pb set))
            role   (.setRole role))
        (.build))))

(defmethod pb->data Protos$Resource
  [^Protos$Resource v]
  (Resource.
   (.getName v)
   (.getType v)
   (.getScalar v)
   (when-let [ranges (.getRanges v)]
     (map pb->data (.getRangeList ranges)))
   (when-let [s (.getSet v)] (pb->data s))
   (.getRole v)))

;; ResourceStatistics
;; ==================

(defrecord ResourceStatistics [timestamp cpus-user-time-secs
                               cpus-system-time-secs cpus-limit
                               cpus-nr-periods cpus-nr-throttled
                               cpus-throttled-time-secs
                               mem-rss-bytes mem-limit-bytes
                               mem-file-bytes mem-anon-bytes
                               mem-mapped-file-bytes perf
                               net-rx-packets net-rx-bytes
                               net-rx-errors net-rx-dropped
                               net-tx-packets net-tx-bytes
                               net-tx-errors net-tx-dropped
                               net-tcp-rtt-microsecs-p50
                               net-tcp-rtt-microsecs-p90
                               net-tcp-rtt-microsecs-p95
                               net-tcp-rtt-microsecs-p99]
  Serializable
  (data->pb [this]
    (-> (Protos$ResourceStatistics/newBuilder)
        (.setTimestamp (double timestamp))
        (cond->
            cpus-user-time-secs       (.setCpusUserTimeSecs
                                       (double cpus-user-time-secs))
            cpus-system-time-secs     (.setCpusSystemTimeSecs
                                       (double cpus-system-time-secs))
            cpus-limit                (.setCpusLimit (double cpus-limit))
            cpus-nr-periods           (.setCpusNrPeriods
                                       (int cpus-nr-periods))
            cpus-nr-throttled         (.setCpusNrThrottled
                                       (int cpus-nr-throttled))
            cpus-throttled-time-secs  (.setCpusThrottledTimeSecs
                                       (double cpus-throttled-time-secs))
            mem-rss-bytes              (.setMemRssBytes (long mem-rss-bytes))
            mem-limit-bytes            (.setMemLimitBytes
                                        (long mem-limit-bytes))
            mem-file-bytes            (.setMemFileBytes
                                        (long mem-file-bytes))
            mem-anon-bytes            (.setMemAnonBytes
                                        (long mem-anon-bytes))
            mem-mapped-file-bytes     (.setMemMappedFileBytes
                                       (long mem-mapped-file-bytes))
            perf                      (.setPerf (pb->data perf))
            net-rx-packets            (.setNetRxPackets (long net-rx-packets))
            net-rx-bytes              (.setNetRxBytes (long net-rx-bytes))
            net-rx-errors             (.setNetRxErrors (long net-rx-errors))
            net-rx-dropped            (.setNetRxDropped (long net-rx-dropped))
            net-tx-packets            (.setNetRxPackets (long net-tx-packets))
            net-tx-bytes              (.setNetRxBytes (long net-tx-bytes))
            net-tx-errors             (.setNetRxErrors (long net-tx-errors))
            net-tx-dropped            (.setNetRxDropped (long net-tx-dropped))
            net-tcp-rtt-microsecs-p50 (.setNetTcpRttMicrosecsP50
                                       (double net-tcp-rtt-microsecs-p50))
            net-tcp-rtt-microsecs-p90 (.setNetTcpRttMicrosecsP90
                                       (double net-tcp-rtt-microsecs-p90))
            net-tcp-rtt-microsecs-p95 (.setNetTcpRttMicrosecsP95
                                       (double net-tcp-rtt-microsecs-p95))
            net-tcp-rtt-microsecs-p99 (.setNetTcpRttMicrosecsP99
                                       (double net-tcp-rtt-microsecs-p99)))
        (.build))))

(defmethod pb->data Protos$ResourceStatistics
  [^Protos$ResourceStatistics s]
  (ResourceStatistics.
   (.getTimestamp s)
   (.getCpusUserTimeSecs s)
   (.getCpusSystemTimeSecs s)
   (.getCpusLimit s)
   (.getCpusNrPeriods s)
   (.getCpusNrThrottled s)
   (.getCpusThrottledTimeSecs s)
   (.getMemRssBytes s)
   (.getMemLimitBytes s)
   (.getMemFileBytes s)
   (.getMemAnonbytes s)
   (.getMemMappedfileBytes s)
   (when-let [p (.getPerf s)] (pb->data p))
   (.getNetRxPackets s)
   (.getNetRxBytes s)
   (.getNetRxErrors s)
   (.getNetRxDropped s)
   (.getNetTxPackets s)
   (.getNetTxBytes s)
   (.getNetTxErrors s)
   (.getNetTxDropped s)
   (.getNetTcpRttMicrosecsP50 s)
   (.getNetTcpRttMicrosecsP90 s)
   (.getNetTcpRttMicrosecsP95 s)
   (.getNetTcpRttMicrosecsP99 s)))

;; ResourceUsage
;; =============

(defrecord ResourceUsage [slave-id framework-id executor-id executor-name
                          task-id statistics]
  Serializable
  (data->pb [this]
    (-> (Protos$ResourceUsage/newBuilder)
        (.setSlaveId (data->pb slave-id))
        (.setFrameworkId (data->pb framework-id))
        (cond->
            executor-id   (.setExecutorId (data->pb executor-id))
            executor-name (.setExecutorName (str executor-name))
            task-id       (.setTaskid (data->pb task-id))
            statistics    (.setStatistics (data->pb statistics)))
        (.build))))

(defmethod pb->data Protos$ResourceUsage
  [^Protos$ResourceUsage usage]
  (ResourceUsage.
   (pb->data (.getSlaveId usage))
   (pb->data (.getFrameworkId usage))
   (when-let [id (.getExecutorId usage)] (pb->data id))
   (.getExecutorName usage)
   (when-let [id (.getTaskId usage)] (pb->data id))
   (when-let [stats (.getResourceStatistics usage)] (pb->data stats))))

;; PerfStatistics
;; ==============

(defrecord PerfStatistics [timestamp duration cycles
                           stalled-cycles-frontend
                           stalled-cycles-backend
                           instructions cache-references
                           cache-misses branches branch-misses
                           bus-cycles ref-cycles cpu-clock task-clock
                           page-faults minor-faults major-faults
                           context-switches cpu-migrations
                           alignment-faults emulation-faults
                           l1-dcache-loads l1-dcache-load-misses
                           l1-dcache-stores l1-dcache-store-misses
                           l1-dcache-prefetches l1-dcache-prefetch-misses
                           l1-icache-loads l1-icache-load-misses
                           l1-icache-prefetches l1-icache-prefetch-misses
                           llc-loads llc-load-misses
                           llc-stores llc-store-misses
                           llc-prefetches llc-prefetch-misses
                           dtlb-loads dtlb-load-misses
                           dtlb-stores dtlb-store-misses
                           dtlb-prefetches dtlb-prefetch-misses
                           itlb-loads itlb-load-misses
                           branch-loads branch-load-misses
                           node-loads node-load-misses
                           node-stores node-store-misses
                           node-prefetches node-prefetch-misses]

  Serializable
  (data->pb [this]
    (let [b (-> (Protos$PerfStatistics/newBuilder)
                (.setTimestamp (double timestamp))
                (.setDuration (double duration)))]

      (when cycles
        (.setCycles b (long cycles)))
      (when stalled-cycles-frontend
        (.setStalledCyclesFrontend b (long stalled-cycles-frontend)))
      (when stalled-cycles-backend
        (.setStalledCyclesBackend b (long stalled-cycles-backend)))
      (when instructions
        (.setInstructions b (long instructions)))
      (when cache-references
        (.setCacheReferences b (long cache-references)))
      (when cache-misses
        (.setCacheMisses b (long cache-misses)))
      (when branches
        (.setBranches b (long branches)))
      (when branch-misses
        (.setBranchMisses b (long branch-misses)))
      (when bus-cycles
        (.setBusCycles b (long bus-cycles)))
      (when ref-cycles
        (.setRefCycles b (long ref-cycles)))
      (when cpu-clock
        (.setCpuClock b (double cpu-clock)))
      (when task-clock
        (.setTaskClock b (double task-clock)))
      (when page-faults
        (.setPageFaults b (long page-faults)))
      (when major-faults
        (.setMajorFaults b (long major-faults)))
      (when context-switches
        (.setContextSwitches b (long context-switches)))
      (when  cpu-migrations
        (.setCpuMigrations b (long cpu-migrations)))
      (when alignment-faults
        (.setAlignmentFaults b (long alignment-faults)))
      (when emulation-faults
        (.setEmulationFaults b (long emulation-faults)))
      (when l1-dcache-loads
        (.setL1DcacheLoads b (long l1-dcache-loads)))
      (when l1-dcache-load-misses
        (.setL1DcacheLoadMisses b (long l1-dcache-load-misses)))
      (when l1-dcache-stores
        (.setL1DcacheStores b (long l1-dcache-stores)))
      (when l1-dcache-store-misses
        (.setL1DcacheStoreMisses b (long l1-dcache-store-misses)))
      (when l1-dcache-prefetches
        (.setL1DcachePrefetches b (long l1-dcache-prefetches)))
      (when l1-dcache-prefetch-misses
        (.setL1DcachePrefetchMisses b (long l1-dcache-prefetch-misses)))
      (when l1-icache-loads
        (.setL1IcacheLoads b (long l1-icache-loads)))
      (when l1-icache-load-misses
        (.setL1IcacheLoadMisses b (long l1-icache-load-misses)))
      (when l1-icache-prefetches
        (.setL1IcachePrefetches b (long l1-icache-prefetches)))
      (when l1-icache-prefetch-misses
        (.setL1IcachePrefetchMisses b (long l1-icache-prefetch-misses)))
      (when llc-loads
        (.setLlcLoads b (long llc-loads)))
      (when llc-load-misses
        (.setLlcLoadMisses b (long llc-load-misses)))
      (when llc-stores
        (.setLlcStores b (long llc-stores)))
      (when llc-store-misses
        (.setLlcStoreMisses b (long llc-store-misses)))
      (when llc-prefetches
        (.setLlcPrefetches b (long llc-prefetches)))
      (when llc-prefetch-misses
        (.setLlcPrefetchMisses b (long llc-prefetch-misses)))
      (when dtlb-loads
        (.setDtlbLoads b (long dtlb-loads)))
      (when dtlb-load-misses
        (.setDtlbLoadMisses b (long dtlb-load-misses)))
      (when dtlb-stores
        (.setDtlbStores b (long dtlb-stores)))
      (when dtlb-store-misses
        (.setDtlbStoreMisses b (long dtlb-store-misses)))
      (when dtlb-prefetches
        (.setDtlbPrefetches b (long dtlb-prefetches)))
      (when dtlb-prefetch-misses
        (.setDtlbPrefetchMisses b (long dtlb-prefetch-misses)))
      (when itlb-loads
        (.setItlbLoads b (long itlb-loads)))
      (when itlb-load-misses
        (.setItlbLoadMisses b (long itlb-load-misses)))
      (when branch-loads
        (.setBranchLoads b (long branch-loads)))
      (when branch-load-misses
        (.setBranchLoadMisses b (long branch-load-misses)))
      (when node-loads
        (.setNodeLoads b (long node-loads)))
      (when node-load-misses
        (.setNodeLoadMisses b (long node-load-misses)))
      (when node-stores
        (.setNodeStores b (long node-stores)))
      (when node-store-misses
        (.setNodeStoreMisses b (long node-store-misses)))
      (when node-prefetches
        (.setNodePrefetches b (long node-prefetches)))
      (when node-prefetch-misses
        (.setNodePrefetchMisses b (long node-prefetch-misses)))
      (.build b))))

(defmethod pb->data Protos$PerfStatistics
  [^Protos$PerfStatistics s]
  (PerfStatistics.
   (.getTimestamp s)
   (.getDuration s)
   (.getCycles s)
   (.getStalledCyclesFrontend s)
   (.getStalledCyclesBackend s)
   (.getInstructions s)
   (.getCacheReferences s)
   (.getCacheMisses s)
   (.getBranches s)
   (.getBranchMisses s)
   (.getBusCycles s)
   (.getRefCycles s)
   (.getCpuClock s)
   (.getTaskClock s)
   (.getPageFaults s)
   (.getMajorFaults s)
   (.getMinorFaults s)
   (.getContextSwitches s)
   (.getCpuMigrations s)
   (.getAlignmentFaults s)
   (.getEmulationFaults s)
   (.getL1DcacheLoads s)
   (.getL1DcacheLoadMisses s)
   (.getL1DcacheStores s)
   (.getL1DcacheStoreMisses s)
   (.getL1DcacheLoadPrefetches s)
   (.getL1DcacheLoadPrefetchMisses s)
   (.getL1IcacheLoads s)
   (.getL1IcacheLoadMisses s)
   (.getL1IcachePrefetches s)
   (.getL1IcachePrefetchMisses s)
   (.getLlcLoads s)
   (.getLlcLoadMisses s)
   (.getLlcStores s)
   (.getLlcStoreMisses s)
   (.getLlcPrefetches s)
   (.getLlcPrefetchMisses s)
   (.getDtlbLoads s)
   (.getDtlbLoadMisses s)
   (.getDtlbStores s)
   (.getDtlbStoreMisses s)
   (.getDtlbPrefetches s)
   (.getDtlbPrefetchMisses s)
   (.getItlbLoads s)
   (.getItlbLoadMisses s)
   (.getBranchLoads s)
   (.getBranchLoadMisses s)
   (.getNodeLoads s)
   (.getNodeLoadMisses s)
   (.getNodeStores s)
   (.getNodeStoreMisses s)
   (.getNodePrefetches s)
   (.getNodePrefetchMisses s)))

;; Request
;; =======

(defrecord Request [slave-id resources]
  Serializable
  (data->pb [this]
    (let [add-resources (fn [builder resources]
                          (doseq [r resources]
                            (.addResources builder (data->pb r)))
                          builder)]
      (-> (Protos$Request/newBuilder)
          (cond-> slave-id (.setSlaveId (data->pb slave-id)))
          (add-resources resources)
          (.build)))))

(defmethod pb->data Protos$Request
  [^Protos$Request req]
  (Request.
   (when-let [id (.getSlaveId req)] (pb->data id))
   (map pb->data (.getResourcesList req))))

;; Offer
;; =====

(defrecord Offer [id framework-id slave-id hostname
                  resources attributes executor-ids]
  Serializable
  (data->pb [this]
    (let [add-resources    (fn [builder resources]
                             (doseq [r resources]
                               (.addResources builder (data->pb r)))
                             builder)
          add-attributes   (fn [builder attributes]
                             (doseq [attr attributes]
                               (.addAttributes builder (data->pb attr)))
                             builder)
          add-executor-ids (fn [builder executor-ids]
                             (doseq [id executor-ids]
                               (.addExecutorIds builder (data->pb id))))]
      (-> (Protos$Offer/newBuilder)
          (.setId (data->pb id))
          (.setFrameworkId (data->pb framework-id))
          (.setSlaveId (data->pb slave-id))
          (.setHostname (str hostname))
          (add-resources resources)
          (add-attributes attributes)
          (add-executor-ids executor-ids)
          (.build)))))

(defmethod pb->data Protos$Offer
  [^Protos$Offer offer]
  (Offer.
   (pb->data (.getId offer))
   (pb->data (.getFrameworkId offer))
   (pb->data (.getSlaveId offer))
   (.getHostname offer)
   (map pb->data (.getResourcesList offer))
   (map pb->data (.getAttributesList offer))
   (map pb->data (.getExecutorIdsList offer))))

;; TaskInfo
;; ========

(defrecord TaskInfo [name task-id slave-id resources executor command
                     container data health-check]
    Serializable
    (data->pb [this]
      (let [add-resources (fn [builder]
                            (doseq [r resources]
                              (.addResources builder (data->pb r)))
                            builder)]
        (-> (Protos$TaskInfo/newBuilder)
            (.setName (str name))
            (.setTaskId (data->pb task-id))
            (.setSlaveId (data->pb slave-id))
            (cond->
                executor     (.setContainer (data->pb container))
                data         (.setData data)
                health-check (.setHealthCheck (data->pb health-check)))
            (add-resources)
            (.build)))))

(defmethod pb->data Protos$TaskInfo
  [^Protos$TaskInfo info]
  (TaskInfo.
   (.getName info)
   (pb->data (.getTaskId info))
   (pb->data (.getSlaveId info))
   (map pb->data (.getResourcesList info))
   (when-let [i (.getExecutor info)] (pb->data i))
   (when-let [c (.getCommand info)] (pb->data c))
   (when-let [c (.getContainer info)] (pb->data c))
   (.getData info)
   (when-let [hc (.getHealthCheck info)] (pb->data hc))
   (when-let [l (.getLabels info)] (pb->data l))
   (when-let [d (.getDiscovery info)] (pb->data d))))

;; TaskState
;; =========

(defmethod pb->data Protos$TaskState
  [^Protos$TaskState status]
  (case status
    Protos$TaskState/TASK_STAGING  :task-staging
    Protos$TaskState/TASK_STARTING :task-starting
    Protos$TaskState/TASK_RUNNING  :task-running
    Protos$TaskState/TASK_FINISHED :task-finished
    Protos$TaskState/TASK_FAILED   :task-failed
    Protos$TaskState/TASK_KILLED   :task-killed
    Protos$TaskState/TASK_LOST     :task-lost
    Protos$TaskState/TASK_ERROR    :task-error
    nil))

;; TaskStatus
;; ==========

(defmethod pb->data Protos$TaskStatus$Source
  [^Protos$TaskStatus$Source status]
  (case status
    Protos$TaskStatus$Source/SOURCE_MASTER   :source-master
    Protos$TaskStatus$Source/SOURCE_SLAVE    :source-slave
    Protos$TaskStatus$Source/SOURCE_EXECUTOR :source-executor))

(defmethod pb->data Protos$TaskStatus$Reason
  [^Protos$TaskStatus$Reason status]
  (case status
    Protos$TaskStatus$Reason/REASON_COMMAND_EXECUTOR_FAILED
    :reason-command-executor-failed
    Protos$TaskStatus$Reason/REASON_EXECUTOR_TERMINATED
    :reason-executor-terminated
    Protos$TaskStatus$Reason/REASON_EXECUTOR_UNREGISTERED
    :reason-executor-unregistered
    Protos$TaskStatus$Reason/REASON_FRAMEWORD_REMOVED
    :reason-framework-removed
    Protos$TaskStatus$Reason/REASON_GC_ERROR
    :reason-gc-error
    Protos$TaskStatus$Reason/REASON_INVALID_FRAMEWORKID
    :reason-invalid-frameworkid
    Protos$TaskStatus$Reason/REASON_INVALID_OFFERS
    :reason-invalid-offers
    Protos$TaskStatus$Reason/REASON_MASTER_DISCONNECTED
    :reason-master-disconnected
    Protos$TaskStatus$Reason/REASON_MEMORY_LIMIT
    :reason-memory-limit
    Protos$TaskStatus$Reason/REASON_RECONCILIATION
    :reason-reconciliation
    Protos$TaskStatus$Reason/REASON_SLAVE_DISCONNECTED
    :reason-slave-disconnected
    Protos$TaskStatus$Reason/REASON_SLAVE_REMOVED
    :reason-slave-removed
    Protos$TaskStatus$Reason/REASON_SLAVE_RESTARTED
    :reason-slave-restarted
    Protos$TaskStatus$Reason/REASON_SLAVE_UNKNOWN
    :reason-slave-unknown
    Protos$TaskStatus$Reason/REASON_TASK_INVALID
    :reason-task-invalid
    Protos$TaskStatus$Reason/REASON_TASK_UNAUTHORIZED
    :reason-task-unauthorized
    Protos$TaskStatus$Reason/REASON_TASK_UNKNOWN
    :reason-task-unknown))

(defrecord TaskStatus [task-id state message source reason
                       data slave-id executor-id timestamp
                       uuid healthy]
  Serializable
  (data->pb [this]
    (-> (Protos$TaskStatus/newBuilder)
        (.setTaskId (data->pb task-id))
        (.setState (data->pb state))
        (cond->
            message     (.setMessage (str message))
            source      (.setSource (data->pb source))
            reason      (.setReason (data->pb reason))
            data        (.setData data)
            slave-id    (.setSlaveId (data->pb slave-id))
            executor-id (.setExecutorId (data->pb executor-id))
            timestamp   (.setTimestamp (double timestamp))
            uuid        (.setUuid uuid)
            healthy     (.setHealthy (boolean healthy)))
        (.build))))

;; Filters
;; =======

(defrecord Filters [refuse-seconds]
  Serializable
  (data->pb [this]
    (-> (Protos$Filters/newBuilder)
        (cond-> refuse-seconds (.setRefuseSeconds (double refuse-seconds)))
        (.build))))

(defmethod pb->data Protos$Filters
  [^Protos$Filters filters]
  (Filters. (.getRefuseSeconds filters)))

;; Environment
;; ===========

(defrecord EnvironmentVariable [name value]
  Serializable
  (data->pb [this]
    (-> (Protos$Environment$Variable/newBuilder)
        (.setName (str name))
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$Environment$Variable
  [^Protos$Environment$Variable var]
  (EnvironmentVariable. (.getName var) (.getValue var)))

(defrecord Environment [variables]
  Serializable
  (data->pb [this]
    (let [builder (Protos$Environment/newBuilder)]
      (doseq [v variables]
        (.addVariables builder (data->pb v)))
      (.build builder))))

;; Parameter
;; =========

(defrecord Parameter [key value]
  Serializable
  (data->pb [this]
    (-> (Protos$Parameter/newBuilder)
        (.setKey (str key))
        (.setValue (str value))
        (.build))))

(defmethod pb->data Protos$Parameter
  [^Protos$Parameter p]
  (Parameter. (.getKey p) (.getValue p)))

(defrecord Parameters [parameter]
  Serializable
  (data->pb [this]
    (let [builder (Protos$Parameters/newBuilder)]
      (doseq [p parameter]
        (.addParameter builder (data->pb p)))
      (.build builder))))

(defmethod pb->data Protos$Parameters
  [^Protos$Parameters p]
  (Parameters. (map pb->data (.getParameterList p))))

;; Credential
;; ==========

(defrecord Credential [principal secret]
  Serializable
  (data->pb [this]
    (-> (Protos$Credential/newBuilder)
        (.setPrincipal (str key))
        (cond-> secret (.setSecret (str secret)))
        (.build))))

(defmethod pb->data Protos$Credential
  [^Protos$Credential c]
  (Credential. (.getPrincipal c) (.getSecret c)))

(defrecord Credentials [credentials]
  Serializable
  (data->pb [this]
    (let [builder (Protos$Credentials/newBuilder)]
      (doseq [c credentials]
        (.addCredentials builder (data->pb c)))
      (.build builder))))

(defmethod pb->data Protos$Credentials
  [^Protos$Credentials c]
  (Credentials. (map pb->data (.getCredentialsList c))))

;; ACL
;; ===

(defmethod pb->data Protos$ACL$Entity$Type
  [^Protos$ACL$Entity$Type type]
  (case type
    Protos$ACL$Entity$Type/SOME :entity-some
    Protos$ACL$Entity$Type/ANY  :entity-any
    Protos$ACL$Entity$Type/NONE :entity-none))

(defrecord ACLEntity [type values]
  Serializable
  (data->pb [this]
    (let [add-values (fn [builder]
                       (doseq [v values]
                         (.addValues builder (str v)))
                       builder)]
      (-> (Protos$ACL$Entity/newBuilder)
          (cond-> type (.setType (data->pb type)))
          (add-values)
          (.build)))))

(defmethod pb->data Protos$ACL$Entity
  [^Protos$ACL$Entity entity]
  (ACLEntity.
   (.getType entity)
   (.getValuesList entity)))

(defrecord ACLRegisterFramework [principals roles]
  Serializable
  (data->pb [this]
    (-> (Protos$ACL$RegisterFramework/newBuilder)
        (.setPrincipals (data->pb principals))
        (.setRoles (data->pb roles))
        (.build))))

(defmethod pb->data Protos$ACL$RegisterFramework
  [^Protos$ACL$RegisterFramework framework]
  (ACLRegisterFramework.
   (pb->data (.getPrincipals framework))
   (pb->data (.getRoles framework))))

(defrecord ACLRunTask [principals users]
  Serializable
  (data->pb [this]
    (-> (Protos$ACL$RunTask/newBuilder)
        (.setPrincipals (data->pb principals))
        (.setUsers (data->pb users))
        (.build))))

(defmethod pb->data Protos$ACL$RunTask
  [^Protos$ACL$RunTask run-task]
  (ACLRunTask.
   (pb->data (.getPrincipals run-task))
   (pb->data (.getUsers run-task))))

(defrecord ACLShutdownFramework [principals framework-principals]
  Serializable
  (data->pb [this]
    (-> (Protos$ACL$ShutdownFramework/newBuilder)
        (.setPrincipals (data->pb principals))
        (.setFrameworkPrincipals (data->pb framework-principals))
        (.build))))

(defmethod pb->data Protos$ACL$ShutdownFramework
  [^Protos$ACL$ShutdownFramework framework]
  (ACLShutdownFramework.
   (pb->data (.getPrincipals framework))
   (pb->data (.getFrameworkPrincipals framework))))

(defrecord ACLs [permissive register-frameworks
                 run-tasks shutdown-frameworks]
  Serializable
  (data->pb [this]
    (let [add-rf (fn [builder]
                   (doseq [rf register-frameworks]
                     (.addRegisterFrameworks builder rf))
                   builder)
          add-rt (fn [builder]
                   (doseq [rt run-tasks]
                     (.addRunTasks builder rt))
                   builder)
          add-sf (fn [builder]
                   (doseq [sf shutdown-frameworks]
                     (.addShutdownFrameworks builder sf))
                   builder)]
      (-> (Protos$ACLs/newBuilder)
          (cond-> permissive (.setPermissive (boolean permissive)))
          (add-rf)
          (add-rt)
          (add-sf)
          (.build)))))

(defmethod pb->data Protos$ACLs
  [^Protos$ACLs acls]
  (ACLs.
   (.getPermissive acls)
   (.getRegisterFrameworks acls)
   (.getRunTasks acls)
   (.getShutdownFrameworks acls)))

;; RateLimit
;; =========

(defrecord RateLimit [qps principal capacity]
  Serializable
  (data->pb [this]
    (-> (Protos$RateLimit/newBuilder)
        (.setPrincipal (str principal))
        (cond->
            qps      (.setQps (double qps))
            capacity (.setCapacity (long capacity)))
        (.build))))

(defmethod pb->data Protos$RateLimit
  [^Protos$RateLimit rl]
  (RateLimit.
   (.getQps rl)
   (.getPrincipal rl)
   (.getCapacity rl)))

(defrecord RateLimits [limits aggregate-default-qps
                       aggregate-default-capacity]
  Serializable
  (data->pb [this]
    (let [add-limits (fn [builder]
                       (doseq [l limits]
                         (.addLimits builder (data->pb l)))
                       builder)]
      (-> (Protos$RateLimits/newBuilder)
          (cond->
              aggregate-default-qps      (.setAggregateDefaultQps
                                          (double aggregate-default-qps))
              aggregate-default-capacity (.setAggregateDefaultCapacity
                                          (long aggregate-default-capacity)))
          (add-limits)
          (.build)))))

(defmethod pb->data Protos$RateLimits
  [^Protos$RateLimits rl]
  (RateLimits.
   (map pb->data (.getLimitsList rl))
   (.getAggregateDefaultQps rl)
   (.getAggregateDefaultCapacity rl)))

;; Volume
;; ======

(defmethod pb->data Protos$Volume$Mode
  [^Protos$Volume$Mode mode]
  (case mode
    Protos$Volume$Mode/RW :volume-rw
    Protos$Volume$Mode/RO :volume-ro))

(defrecord Volume [container-path host-path mode]
  Serializable
  (data->pb [this]
    (-> (Protos$Volume/newBuilder)
        (.setContainerPath (str container-path))
        (.setMode (data->pb mode))
        (cond-> host-path (.setHostPath (str host-path))))))

;; ContainerInfo
;; =============

(defmethod pb->data Protos$ContainerInfo$Type
  [^Protos$ContainerInfo$Type type]
  (case type
    Protos$ContainerInfo$Type/DOCKER :container-type-docker
    Protos$ContainerInfo$Type/MESOS  :conatiner-type-mesos))

(defrecord PortMapping [host-port container-port protocol]
    Serializable
    (data->pb [this]
      (-> (Protos$ContainerInfo$DockerInfo$PortMapping/newBuilder)
          (.setHostPort (int host-port))
          (.setContainerPort (int container-port))
          (cond-> protocol (.setProtocol protocol))
          (.build))))

(defmethod pb->data Protos$ContainerInfo$DockerInfo$PortMapping
  [^Protos$ContainerInfo$DockerInfo$PortMapping pm]
  (PortMapping.
   (.getHostPort pm)
   (.getContainerPort pm)
   (.getProtocol pm)))

(defmethod pb->data Protos$ContainerInfo$DockerInfo$Network
  [^Protos$ContainerInfo$DockerInfo$Network network]
  (case network
    Protos$ContainerInfo$DockerInfo$Network/HOST   :docker-network-host
    Protos$ContainerInfo$DockerInfo$Network/BRIDGE :docker-network-bridge
    Protos$ContainerInfo$DockerInfo$Network/NONE   :docker-network-none))

(defrecord DockerInfo [image network port-mappings
                       privileged parameters]
  Serializable
  (data->pb [this]
    (let [add-port-mappings (fn [builder]
                              (doseq [pm port-mappings]
                                (.addPortMappings builder (data->pb pm)))
                              builder)
          add-parameters    (fn [builder]
                              (doseq [p parameters]
                                (.addParameters builder (data->pb p)))
                              builder)]
      (-> (Protos$ContainerInfo$DockerInfo/newBuilder)
          (.setImage image)
          (cond->
              network       (.setNetwork (data->pb network))
              privileged    (.setPrivileged (boolean privileged))

              )
          (add-port-mappings)
          (add-parameters)
          (.build)))))

(defrecord ContainerInfo [type volumes hostname docker]
  Serializable
  (data->pb [this]
    (let [add-volumes (fn [builder]
                        (doseq [v volumes]
                          (.addVolumes builder v))
                        builder)]
      (-> (Protos$ContainerInfo/newBuilder)
          (.setType (data->pb type))
          (cond->
              hostname (.setHostname (str hostname))
              docker   (.setDocker (data->pb docker)))
          (add-volumes)
          (.build)))))

;; Safe for the common stuff in extend-protocol, this marks the end of
;; messages defined in mesos.proto

;; Handle serialization of Status, Value.Type and TaskState from
;; keywords, as well as Scalars from their value.

(extend-protocol Serializable
  java.lang.Double
  (data->pb [this]
    (-> (Protos$Value$Scalar/newBuilder)
        (.setValue this)
        (.build)))
  clojure.lang.PersistentHashSet
  (data->pb [this]
    (let [add-items (fn [builder items]
                      (doseq [item items]
                        (.addItem builder (str item)))
                      builder)]
      (-> (Protos$Value$Set/newBuilder) (add-items (seq this)) (.build))))
  java.lang.String
  (data->pb [this]
    (-> (Protos$Value$Text/newBuilder) (.setValue this) (.build)))
  clojure.lang.Keyword
  (data->pb [this]
    (case this
      :driver-not-started       Protos$Status/DRIVER_NOT_STARTED
      :driver-running           Protos$Status/DRIVER_RUNNING
      :driver-aborted           Protos$Status/DRIVER_ABORTED
      :driver-stopped           Protos$Status/DRIVER_STOPPED
      :task-staging             Protos$TaskState/TASK_STAGING
      :task-starting            Protos$TaskState/TASK_STARTING
      :task-running             Protos$TaskState/TASK_RUNNING
      :task-finished            Protos$TaskState/TASK_FINISHED
      :task-failed              Protos$TaskState/TASK_FAILED
      :task-killed              Protos$TaskState/TASK_KILLED
      :task-lost                Protos$TaskState/TASK_LOST
      :task-error               Protos$TaskState/TASK_ERROR
      :value-scalar             Protos$Value$Type/SCALAR
      :value-ranges             Protos$Value$Type/RANGES
      :value-set                Protos$Value$Type/SET
      :value-text               Protos$Value$Type/TEXT
      :entity-some              Protos$ACL$Entity$Type/SOME
      :entity-any               Protos$ACL$Entity$Type/ANY
      :entity-none              Protos$ACL$Entity$Type/NONE
      :volume-rw                Protos$Volume$Mode/RW
      :volume-ro                Protos$Volume$Mode/RO
      :docker-network-host      Protos$ContainerInfo$DockerInfo$Network/HOST
      :docker-network-bridge    Protos$ContainerInfo$DockerInfo$Network/BRIDGE
      :docker-network-none      Protos$ContainerInfo$DockerInfo$Network/NONE
      :container-type-docker    Protos$ContainerInfo$Type/DOCKER
      :container-type-mesos     Protos$ContainerInfo$Type/MESOS

      ;; These are too wide and mess up indenting!
      :reason-command-executor-failed
      Protos$TaskStatus$Reason/REASON_COMMAND_EXECUTOR_FAILED
      :reason-executor-terminated
      Protos$TaskStatus$Reason/REASON_EXECUTOR_TERMINATED
      :reason-executor-unregistered
      Protos$TaskStatus$Reason/REASON_EXECUTOR_UNREGISTERED
      :reason-framework-removed
      Protos$TaskStatus$Reason/REASON_FRAMEWORK_REMOVED
      :reason-gc-error
      Protos$TaskStatus$Reason/REASON_GC_ERROR
      :reason-invalid-frameworkid
      Protos$TaskStatus$Reason/REASON_INVALID_FRAMEWORKID
      :reason-invalid-offers
      Protos$TaskStatus$Reason/REASON_INVALID_OFFERS
      :reason-master-disconnected
      Protos$TaskStatus$Reason/REASON_MASTER_DISCONNECTED
      :reason-memory-limit
      Protos$TaskStatus$Reason/REASON_MEMORY_LIMIT
      :reason-reconciliation
      Protos$TaskStatus$Reason/REASON_RECONCILIATION
      :reason-slave-disconnected
      Protos$TaskStatus$Reason/REASON_SLAVE_DISCONNECTED
      :reason-slave-removed
      Protos$TaskStatus$Reason/REASON_SLAVE_REMOVED
      :reason-slave-restarted
      Protos$TaskStatus$Reason/REASON_SLAVE_RESTARTED
      :reason-slave-unknown
      Protos$TaskStatus$Reason/REASON_SLAVE_UNKNOWN
      :reason-task-invalid
      Protos$TaskStatus$Reason/REASON_TASK_INVALID
      :reason-task-unauthorized
      Protos$TaskStatus$Reason/REASON_TASK_UNAUTHORIZED
      :reason-task-unknown
      Protos$TaskStatus$Reason/REASON_TASK_UNKNOWN

      ;; default
      nil)))

;; By default, yield the original payload.

(defmethod pb->data :default
  [this]
  this)