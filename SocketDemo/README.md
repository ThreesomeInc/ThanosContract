## Demo provider.ProviderMain & Consumer

**Target to simulate:**

Two consumer point to same provider and sending diff msg with same schema.


### Request Schema

| id | name     | type | length | regex             |
|----|----------|------|--------|-------------------|
| 1  | Flag     | CHAR | 1      | 0|1               |
| 2  | TranCode | CHAR | 2      | AA|BB|CC|DD|EE|FF |


### Response Schema

| id | name     | type | length | regex             |
|----|----------|------|--------|-------------------|
| 1  | Status   | NUM  | 3      | \d{3}             |
| 2  | ResponseTime | CHAR | 8  | \d{8}             |

### Test Case

```
name: test case 1
req: 0AA
res: 20020191212

name: test case 2
req: 1AA
res: 40420191212

```  



