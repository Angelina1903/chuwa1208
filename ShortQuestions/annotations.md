## Annotations Learned from Spring Data (Class & Homework)

### Core Spring / Spring Boot
- `@SpringBootApplication` – Entry point of Spring Boot application
- `@Component` – Marks a Spring-managed component
- `@Service` – Service-layer component (business logic)
- `@Repository` – DAO layer, enables exception translation
- `@Autowired` – Dependency Injection

### Web / REST
- `@RestController` – RESTful controller
- `@RequestMapping` – Base URL mapping
- `@GetMapping` – Handle HTTP GET requests
- `@PostMapping` – Handle HTTP POST requests
- `@PutMapping` – Handle HTTP PUT requests
- `@DeleteMapping` – Handle HTTP DELETE requests
- `@PathVariable` – Bind URI path variable
- `@RequestParam` – Bind query parameter
- `@RequestBody` – Bind request body to object

### JPA / Hibernate
- `@Entity` – Marks a JPA entity
- `@Table` – Specifies database table name
- `@Id` – Primary key
- `@GeneratedValue` – Auto-generate primary key
- `@Column` – Column mapping
- `@OneToMany` – One-to-many relationship
- `@ManyToOne` – Many-to-one relationship
- `@ManyToMany` – Many-to-many relationship
- `@JoinColumn` – Foreign key column
- `@JoinTable` – Join table for many-to-many
- `@MappedSuperclass` – Base entity class
- `@Enumerated` – Map enum to column
- `@Transactional` – Transaction management
- `@Cacheable` – Enable second-level cache
- `@NamedQuery` – Static JPQL query
- `@NamedQueries` – Multiple named queries
- `@Query` – Custom JPQL or SQL query
- `@Modifying` – Update/Delete query
- `@Temporal` – Date/time mapping
- `@CreationTimestamp` – Auto set create time
- `@UpdateTimestamp` – Auto set update time

### Repository / Spring Data JPA
- `JpaRepository` – CRUD + paging + sorting
- `PagingAndSortingRepository` – Pagination & sorting


### Exception Handeling
- `@ExceptionHandler`
    * Method Level 
    * used to handle the specific exceptions and sending the custom responses to the client 
- `@ControllerAdvice`
    * Steps Class Level make this class be a bean
- `@ResponseStatus`
    * Maps an exception to a specific HTTP status code

### Validation
- `@Valid`
    * Triggers validation on request body or method arguments
    * Usually used in controller method parameters
    * Other validation annotations: `@NotEmpty`, `@NotBlank`, `@Size`, `@Pattern`, `@Email`

### Bean Configuration / IoC
- `@Bean`
    * Registers a bean using a method in a `@Configuration` class
    * Commonly used for third-party libraries (e.g. ModelMapper)

- `@Configuration`
    * Marks a class as a Java-based Spring configuration class

- `@Primary`
    * Indicates the default bean when multiple implementations exist

- `@Qualifier`
    * Specifies which bean to inject when multiple candidates are available


### Dependency Injection
- `@Autowired`
    * Enables automatic dependency injection by type

### Spring MVC – Controller / View
- `@Controller`
    * Marks a class as a Spring MVC controller
    * Typically returns a **view name** (e.g. JSP)

- `@ResponseBody`
    * Writes the return value directly to the HTTP response body
    * Often used to return JSON/string directly (without a view)

### Spring Security
- `@EnableWebSecurity`
    * Enables Spring Security configuration

- `@PreAuthorize`
    * Method-level authorization using expressions (role/permission checks)