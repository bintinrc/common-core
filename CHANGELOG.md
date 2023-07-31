# Change log

<hr>

## [1.3.17-RC1] 2023-07-31

### Added

- API Core - Operator bulk force success below orders with cod collected {string}:
- API Core - Operator force success waypoint with cod collected as {string} using route manifest:

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