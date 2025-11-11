## Sistema de Biblioteca Digital (Java) com Padrões de Projeto

Padrões aplicados:
- Singleton: `biblioteca.Biblioteca`
- Factory Method: `biblioteca.UsuarioFactory`
- Strategy: `biblioteca.emprestimo.MultaStrategy` com `AlunoMulta` e `ProfessorMulta`
- Observer: `biblioteca.notificacao.Observador` e notificadores (Email, SMS, Push)
- Decorator (extra): `biblioteca.decorator.LivroDecorator`, `LivroDigital`, `LivroImpresso`

### Estrutura
Diretório `src/` com pacotes conforme enunciado.

### Como compilar e executar
No diretório raiz (esta pasta), execute:

Windows (cmd):
```
javac -d out src\Main.java src\biblioteca\*.java src\biblioteca\emprestimo\*.java src\biblioteca\notificacao\*.java src\biblioteca\decorator\*.java
java -cp out Main
```

PowerShell:
```
javac -d out .\src\Main.java .\src\biblioteca\*.java .\src\biblioteca\emprestimo\*.java .\src\biblioteca\notificacao\*.java .\src\biblioteca\decorator\*.java
java -cp out Main
```

### Observações
- O cálculo de multa usa Strategy e diferencia Aluno/Professor (Admin isento).
- O Observer envia mensagens simuladas via `System.out.println`.
- O Decorator adiciona atributos a livros sem modificar a classe base.


