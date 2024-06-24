# Change log

## [1.5.4] 2024-05-28

- New Step `API Core - evict driver's cod limit cache:`
- New Step `DB Core - Operator verify total orders under the batch_id: {string} is {int}`
- Remove legacy shipper id on reservation
- New fields on `edit_delivery_order/ParcelJob` bean
- New Step `API Route - Operator Fails to Edit Route Waypoint on Zonal Routing Edit Route:`
- Deprecate `core.waypoints` table and redirect existing DB steps to `route.waypoints` table

<hr>

## [1.5.3] 2024-05-14

Add new optional key `refresh (true/false)` to the `API Core - verify driver's total cod:` step

New Step `API Route - Operator add parcel to the route using data below:`
This step will replace these Core step:
- API Core - Operator add parcel to the route using data below:
- API Core - Operator new add parcel to DP holding route:

New Step `DB Route - wait until job_waypoints table is populated for job id {string}`

<hr>

### [1.5.2] 2024-04-18

- Remove duplication of setting the value of delivery_verification_mode in get order step
- Drop legacy_id column from JobWaypoint hibernate model (ROUTE-961)
- Upgrade to common-base 1.5.3
- Upgrade to common-auth 1.5.2

New step `API Route - Operator Edit Route Waypoint on Zonal Routing Edit Route:
This step will replace Core's step:
- API Core - Operator Edit Route Waypoint on Zonal Routing Edit Route:

<hr>

### [1.5.1] 2024-03-05

Sync skipped commits from 1.4.1 to 1.5.1

<hr>

### [1.5.0] 2024-02-09

- Upgrade to common-base 1.5.0
- Upgrade to common-auth 1.5.0
 
<hr>

## [1.41.1] 2024-03-05

### New steps

- API Core - update order delivery type:

## [1.4.0] 2024-02-16

### New steps

- DB Routing Search - verify transactions record is hard deleted:

### Modified Steps

- The step `API Core - Operator update priority level for the reservation using data below:` now
  needs mandatory `globalShipperId` in dataTable
- The step `API Route - Operator run FM auto route cron job for date {string}` now can resolve dates
  given from keys (ScenarioStorage)

### Removed deprecated APIs

- core/2.0/orders/{orderId}/collect
- core/2.0/orders/{orderId}/dropoff
- core/2.0/orders/{orderId}/lodgein
- core/3.0/routes/monitoring
- core/3.0/routes/monitoring/routes/{routeId}
- core/graphql/route
- core/messaging/orders/sms
- core/messaging/orders/sms/history?tracking_id={trackingId}

### Removed deprecated classes

- CustomerCollectRequest()
- LodgeInRequest()
- MessagingClient()
- MessagingHistoryResponse()
- RouteMonitoringClient()
- RouteMonitoringResponse()

### Removed Steps

- API Core - Customer collect from dp:
- API Core - Operator gets all the SMS notification by Tracking ID {string}
- API Core - Operator lodge in order at dp:
- API Core - Operator perform dp drop off with order id {string}
- API Core - Operator send sms with following data

<hr>

## [1.3.25] 2024-01-08

### Added

New Method :
-Method : updateReservationDetails, no step definition (step definition on DriverApp side for specific case)

New Steps:
- `API Core - Operator search pickup using data below:`, to search pickup by some filters
- `DB Routing Search - verify transactions record:`, to verify transaction record in routingSearch DB

Model class:
- Add ticketId and orderOutcome fields in Event Detail model
- Add new model for RPM Page

new variable:
- Add global shipper id variable for reservation creation

New KEYS:
- KEY_CORE_LIST_OF_PICKUPS

Fixed code:
- remove method for updateOrderPickupAddress in orderClient

Error Classification:
- Catch casting exception error 

<hr>

## [1.3.24] 2023-12-11

### Added

Error classification:

- Throw `NvTestCoreOrderKafkaLagException()` instead of `AssertionError()`
  for `API Core - Operator get order details for tracking order` step.

Model class name disambiguation:

- Renamed `co.nvqa.common.core.model.order.Tag` to `co.nvqa.common.core.model.order.OrderTag`
- Renamed `co.nvqa.common.core.model.route.Tag` to `co.nvqa.common.core.model.route.RouteTag`

New KEYS:

- KEY_CORE_LIST_OF_CREATED_ROUTE_TAGS
- KEY_CORE_SMS_NOTIFICATIONS_SETTINGS
- KEY_CORE_PRINTER_SETTINGS
- KEY_CORE_CREATED_THIRD_PARTY_SHIPPER
- KEY_CORE_CREATED_THIRD_PARTY_SHIPPER_EDITED

New Steps:

- API Core - Operator adds new printer using data below:
- API Core - Operator gets SMS notifications settings
- API Core - Operator verify the {string} Third Party Shipper is searchable
- API Route - Operator add reservations to {string}:
- API Route - Operator add transactions to {string}:
- API Route - create new route tag:
- DB Core - verifies that latitude is in range -90 to 90 and longitude is in range -180 to 180 for
  waypointId {string}

New Hooks:

- @DeletePrinterV2
- @DeleteRouteTagsV2
- @DeleteThirdPartyShippersV2
- @RestoreSmsNotificationsSettingsV2

<hr>

## [1.3.23] 2023-11-27

### Added
- Add datatable example in edit route details
- Add field for cv2 migration
- add send message api
- Fix reschedule request fields
- Create editRouteRequest model

<hr>

## [1.3.22] 2023-11-13

### Added

- API Core - Operator add multiple parcels to route {string} with type {string} using data below:
- Add isOpenBox field to order model

<hr>

## [1.3.21] 2023-11-06

### Added

- API Core - Operator fail to create new route from zonal routing using data below:
- API Core - Operator recalculate order price:
- API Core - van inbound order:
- API Route - set tags to route:
- DB Core - verifies cod_collections record was not created:
- DB Core - verifies service_level in orders table
- DB Core - verifies service_type in orders table
- New Exception class `NvTestCoreOrderKafkaLagException` extends `NvTestEnvironmentException`

### Fixed/Modified

- Added NvTestCoreOrderKafkaLagException to steps:
  - API Core - Operator get order details for tracking order {string} with granular status
    {string}
  - API Core - Operator verify Auto AV event
  - API Core - Operator verify that event is published with correct details:
  - API Core - Operator verify that {string} event is published for order id {string}
  - API Core - Wait for following order state:

- API Core - cancel order {value}
- API Core - Operator check order {string} have the following Tags:
- Add DRIVER_DAILY_COD_LIMIT key to @After("@RestoreSystemParams")
- API Core - Operator create new route using data below:

<hr>

## [1.3.20] 2023-10-07

### Added

- API Core - Add order tracking id to 3PL with the following info:
- API Core - Operator create sales person:
- API Core - Operator get reservation using filter with data below:
- API Route - Operator get created Reservation Group params:
- DB Core - operator get transaction records with:
- Migrated SalesClient.java
- New CoreTestUtils method: generateUniqueId()
- New hook @DeleteCreatedSalesPerson
- Removed unused test suite

<hr>

## [1.3.19] 2023-09-05

### Added

- API Core - Operator Outbound Scan parcel using data below:
- API Core - Operator check order {string} have the following Tags:
- API Core - Operator update parcel size:
- API Core - set system parameter:
- API Core - verify driver's total cod:
- API Route - delete routes:
- DB Core - Operator search {string} Orders with {string} Status and {string} Granular Status
- DB Core - soft delete route {value}
- DB Core - verify total COD for driver:
- DB Route - get route_logs record for driver id {string}

<hr>

## [1.3.18] 2023-08-14

### Added

- API Core - Operator create new COD Inbound for created order:
- API Core - Operator verify pods in pickup details of reservation id below:
- API Core - Operator start the route with following data:
- DB Core - get order_delivery_details record for order {value}
- DB Route - verify that sr_coverages record is not created for {string} area
- DB Route - verify that sr_coverages record is not created for {string} coverageId
- DB Route - fetch coverage id for {value} area

<hr>

## [1.3.17] 2023-08-08

### Added

- RouteResponse.java - Add Hub to represent the hub associated with the route
- API Core - Operator bulk force success below orders with cod collected {string}:
- API Core - Operator force success waypoint with cod collected as {string} using route manifest:
- API Route - Operator run FM PAJ auto route cron job for date {string}

<hr>

## [1.3.16] 2023-07-28

### Added

- API Core - Operator delete order with order id {string}
- API Core - Operator success reservation for id {string}
- API Core - Operator update reservation using data below:
- DB Core - verify orders records are hard-deleted in order_delivery_verifications table:
- DB Core - verify orders records are hard-deleted in order_details table:
- DB Core - verify orders records are hard-deleted in orders table:
- DB Core - verify orders records are hard-deleted in transactions table:
- DB Route - verifies that route_qa_gl.sr_keywords multiple records are created:
- DB Route - verifies that route_qa_gl.sr_keywords multiple records were deleted:
- DB Route - verifies that route_qa_gl.sr_keywords record is created:
- DB Route - verifies that route_qa_gl.sr_keywords record was deleted:

<hr>

## [1.3.15] 2023-07-17

### Added

- API Core - Wait for following order state:
- API Core - Verifies order state:
- API Core - wait for order state:
- API Core - Operator post Lazada 3PL using data below:

<hr>

## [1.3.14] 2023-07-10

### Added

- API Core - Update priority level of an order:

### Deleted

- DB Core - get order id of incomplete orders for legacy shipper id {string}

<hr>

## [1.3.13] 2023-06-30

### Added

- API Route - add references to Route Group:
- API Route - create route group:
- DB Route - get waypoint id for job id {string}
- DB Core - Operator verifies inbound_scans record:
- DB Route - get waypoint id for job id {string}
- @DeleteRouteGroupsV2

### Deleted

- DB Core - get waypoint id for job id {string}

<hr>

## [1.3.12] 2023-06-23

### Added

- API Core - Operator update delivery order details:
- API Route - Operator add multiple waypoints to route:
- API Core - Operator refresh the following orders
- DB Core - get order id of incomplete orders for legacy shipper id {string}
- @DeleteCoverageV2

<hr>

## [1.3.11] 2023-06-17

### Added

- API Core - Operator van inbound order:
- API Core - Operator van inbound and start route using data:
- DB Core - Operator verifies cod_collections record:

<hr>

## [1.3.10] 2023-06-15

### Added

- API Core - Operator failed to add reservation to route using data below:
- API Core - Operator failed to remove reservation id {string} from route

<hr>

## [1.3.9] 2023-06-14

### Added

- DB Core - get order by order tracking id {string}

<hr>

## [1.3.8] 2023-06-08

### Added

- DB Core - verify order id {string} orders.data.belt_dimensions record:

<hr>

## [1.3.7] 2023-06-08

### Added

- API Core - Operator parcel transfer to a new route:

<hr>

## [1.3.6] 2023-06-08

### Added

- API Core - Operator update order COP:

<hr>

## [1.3.5] 2023-06-05

### Added

- API Core - Operator add/update order COD amount:

<hr>

## [1.3.4] 2023-06-01

### Added

- Hook: @After("@ForceSuccessCommonV2")
- Hook: @After("@DeleteRoutes")

<hr>

## [1.3.3] 2023-05-31

### Added

- API Core - Operator verify that event is published with correct details:
- API Core - Customer collect from dp:

<hr>

## [1.3.2] 2023-05-29

### Added

- API Core - force cancel order {value}

<hr>

## [1.3.2] 2023-05-29

### Added

- API Core - Operator update order granular status:
- API Route - Operator create new coverage:

<hr>

## [1.3.1] 2023-05-29

### Added

- API Core - Operator update order granular status:
- API Route - Operator create new coverage:

<hr>

## [1.2.44] 2023-05-25

### Added
 
- API Core - tags parcel with the below tag

<hr>

## [1.2.43] 2023-05-25

### Added

- API Core - Operator verify that {string} event is published for order id {string}
- API Core - update order dimensions:

<hr>

## [1.2.40] 2023-05-24

### Added

- API Core - Operator bulk add pickup jobs to the route using data below:

<hr>

## [1.2.39] 2023-05-24

### Added

New Steps:

- API Core - Operator revert rts order:

<hr>

## [1.2.37] 2023-05-23

### Added

New Steps:

- API Core - resume order {value}
- DB Core - get all waypoint ids of jobs
- DB Core - verify order by Stamp ID:
- DB Core - verify order_delivery_verifications record:
- DB Core - verify cods record:

<hr>