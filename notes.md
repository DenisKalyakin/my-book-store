# Notes

- Onion architecture ([picture](https://herbertograca.files.wordpress.com/2018/11/100-explicit-architecture-svg.png?w=1200)).
- Anemic vs. Rich Domain Objects ([article](https://www.baeldung.com/java-anemic-vs-rich-domain-objects)).
- URI vs URL vs URN ([stackoverflow](https://stackoverflow.com/questions/4913343/what-is-the-difference-between-uri-url-and-urn))
- DDD Aggregate Pattern ([article]https://martinfowler.com/bliki/DDD_Aggregate.html)

```csharp
class User
{
    User(IReadOnlyCollection<Book> rentedBooks, param1, param2, param3, param4, ...) { ... }
}
class Book { }

class UserService
{
    UserAggregate Get(string userId);
}

class AlternativeUserAggregateImplementation : User
{
    UserAggregate(IReadOnlyCollection<Book> rentedBooks, param1, param2, param3, param4, ...) : base(param1, param2, param3, param4, ...) { ... }
    
    IReadOnlyCollection<Book> RentedBooks { get; }
}

class UserAggregate
{
    User Root { get; }
    IReadOnlyCollection<Book> RentedBooks { get; }
    
    AlternativeUserAggregateImplementation(User root, IReadOnlyCollection<Book> rentedBooks) { ... }
}
```

- Feature Approach (TODO: link).
- Global Exception Handling Problems in API (TODO).
- Types of exceptions (TODO).
- Premature Generalization ([thread](https://wiki.c2.com/?PrematureGeneralization)).
- Transactions ACID ([article](https://www.databricks.com/glossary/acid-transactions))
- [Блог](https://sergeyteplyakov.blogspot.com/) Теплякова
- [Tech Radar](https://www.thoughtworks.com/radar)