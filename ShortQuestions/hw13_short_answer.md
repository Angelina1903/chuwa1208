# Chuwa 0128 HW13 - Short Answer
Delin Liang <br>
Due: Feb 04, 2026 <br>

_Note: for Q1, please check the `annotaitons.md` file_

---
### Q2 - What is Aspect Oriented Programming, what does "aspect" mean? Explain its use cases in detail?
**Aspect-Oriented Programming (AOP)** is a programming paradigm that improves modularity by separating cross-cutting concerns from core business logic
- Cross-cutting concerns affect multiple layers or modules of an application
- Examples: logging, security, transaction management, exception handling

#### What does “aspect” mean?
- An aspect is a modular unit of a cross-cutting concern
- It encapsulates behavior that should be applied to multiple methods or classes
- Defined once and applied declaratively without changing business code

#### Use cases of Spring AOP
1. **Logging**
    - Method entry / exit logs
    - Performance measurement and debugging
    - Platform or infrastructure teams may use Spring AOP to build standardized logging frameworks shared across applications

2. **Transaction management**
    - Automatically manage commit and rollback
    - Keeps business logic clean

3. **Security**: Authentication and authorization checks before method execution

4. **Exception handling**: Centralized error logging and handling

5. **Performance monitoring**: Measure execution time and detect bottlenecks

6. **Auditing**: Track sensitive operations and access history

---
### Q3 - What are the advantages and disadvantages of Spring AOP?

#### Advantages
**Separation of concerns**
- Keeps business logic independent from cross-cutting concerns (e.g. logging, security)

**Reduced code duplication**
- Cross-cutting logic is written once and reused across multiple classes

**Improved code readability and maintainability**
- Business methods stay clean and focused

**Centralized management**
- Logging, transactions, and security rules can be modified in one place

**Non-intrusive**
- Existing code does not need to be modified to add cross-cutting behavior

#### Disadvantage
**Harder to debug**
- Execution flow is less obvious due to proxy-based method interception

**Limited scope**
- Spring AOP only supports method-level interception (not field or constructor)

**Runtime overhead**
- Proxy creation and method interception add slight performance cost

**Hidden behavior**
- Method behavior may change without visible code changes, reducing code transparency

---
### Q4 - Compare Spring AOP vs Java Reflection vs Spring Interceptor.

#### 1. Purpose / Typical use
**Spring AOP**
- General mechanism for cross-cutting concerns at the method level (logging, transactions, security, monitoring)

**Java Reflection**
- Low-level Java capability to inspect/invoke classes/methods at runtime (framework internals, dynamic binding, generic utilities)

**Spring Interceptor**
- Web-layer mechanism to intercept HTTP request lifecycle around a controller (auth checks, request logging, locale, rate limiting at web layer)

#### 2. Where it runs (scope)
**Spring AOP**
- Runs around Spring-managed bean method calls (typically service/repo/controller methods), via proxies. 

**Java Reflection**
- Runs wherever you call it; not tied to Spring or web; can invoke any accessible method at runtime

**Spring Interceptor**
- Runs in the Spring MVC request pipeline: before controller, after controller, after completion (request/response level)

#### 3. Trigger mechanism
**Spring AOP**
- Triggered by pointcuts matching join points (usually method execution). Advice can be `@Before`/`@After`/`@Around`/...

**Java Reflection**
- Triggered by explicit code: Method.invoke(...), Class.forName(...), etc.

**Spring Interceptor**
- Triggered automatically per request when registered, e.g. preHandle / postHandle / afterCompletion

#### 4. Best used for
**Spring AOP**
- For method-level cross-cutting concerns across beans

**Java Reflection**
- For HTTP request lifecycle concerns

**Spring Interceptor**
- For dynamic runtime invocation/inspection (mostly framework-level needs)

---
### Q5 - Explain following concept in your own words, you may include code snippet as part of your answer. 1. Aspect 2. PointCut 3. JoinPoint 4. Advice

#### Aspect
An Aspect is a class that encapsulates cross-cutting logic (e.g. logging or security) and applies it to multiple methods. It defines what to do and where to apply it

#### PointCut
A PointCut defines which methods should be intercepted. It is an expression that matches specific method executions

#### JoinPoint
A JoinPoint represents a specific method execution where an aspect is applied. It provides runtime information such as method name and arguments

#### Advice
Advice defines when the aspect logic runs (before, after, or around a method). It contains the actual code executed at a join point

---
### Q6 - How do we declare a pointcut, can we declare it without annotating an empty method? Name some expressions to do it

#### Ways to decalare a PoitCut
1) Using `@Pointcut` with an empty method (common)
    - Define the pointcut expression once and reuse it in multiple advices
2) Without an empty method (inline pointcut)
    - Declare a pointcut directly inside an advice annotation.
    - Useful for simple or one-off cases


#### Common pointcut expressions
`execution` – match method execution (most common)
`within` – match all methods inside a package or class
`@annotation` – match methods with a specific annotation
`args` – match methods with specific parameter types
`bean` – match Spring beans by name
`this` / `target` – match proxy object or target object type

### Q7 - Compare different types of advices in Spring AOP
#### `@Before`
- Runs before the target method executes
- Cannot modify the return value or stop method execution
- Common use: validation, logging input parameters

#### `@After`
- Runs after the target method finishes (success or exception)
- Does not know the return value
- Common use: resource cleanup

#### `@AfterReturning`
- Runs after the method successfully returns
- Can access and inspect the return value
- Common use: logging results, post-processing

#### `@AfterThrowing`
- Runs only when the method throws an exception
- Can access the thrown exception
- Common use: exception logging and monitoring

#### `@Around`
- Runs before and after the method execution
- Can control whether the method executes
- Can modify arguments and return value
- Requires ProceedingJoinPoint
- Most powerful but also most complex

