
import java.util.ArrayList;

public class Function {
    public ArrayList<String> terms;
    public String expression;
    private final Node root;

    public Function(String func) {
        if (func == null) {
            throw new IllegalArgumentException("Function expression cannot be null");
        }
        this.expression = func.replaceAll("\\s+", "");
        if (this.expression.isEmpty()) {
            throw new IllegalArgumentException("Function expression cannot be empty");
        }

        this.terms = Utils.term_splitter(this.expression);
        this.root = Parser.parse(this.expression);
    }

    public double output(double x) {
        return root.evaluate(x);
    }

    public int getOrder() {
        return root.degree();
    }

    public int getTermCount() {
        return terms.size();
    }

    @Override
    public String toString() {
        return this.expression;
    }

    private interface Node {
        double evaluate(double x);
        int degree();
    }

    private static class ConstNode implements Node {
        private final double value;

        ConstNode(double value) {
            this.value = value;
        }

        @Override
        public double evaluate(double x) {
            return value;
        }

        @Override
        public int degree() {
            return 0;
        }
    }

    private static class VarNode implements Node {
        @Override
        public double evaluate(double x) {
            return x;
        }

        @Override
        public int degree() {
            return 1;
        }
    }

    private static class BinaryOpNode implements Node {
        private final char op;
        private final Node left;
        private final Node right;

        BinaryOpNode(char op, Node left, Node right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        @Override
        public double evaluate(double x) {
            return switch (op) {
                case '+' -> left.evaluate(x) + right.evaluate(x);
                case '-' -> left.evaluate(x) - right.evaluate(x);
                case '*' -> left.evaluate(x) * right.evaluate(x);
                case '/' -> left.evaluate(x) / right.evaluate(x);
                case '^' -> Math.pow(left.evaluate(x), right.evaluate(x));
                default -> throw new IllegalArgumentException("Unsupported operator: " + op);
            };
        }

        @Override
        public int degree() {
            return switch (op) {
                case '+' -> Math.max(left.degree(), right.degree());
                case '-' -> Math.max(left.degree(), right.degree());
                case '*' -> left.degree() + right.degree();
                case '/' -> right instanceof ConstNode ? left.degree() : -1;
                case '^' -> {
                    if (right instanceof ConstNode) {
                        double exp = ((ConstNode) right).value;
                        if (exp == Math.floor(exp) && exp >= 0) {
                            yield left.degree() * (int) exp;
                        }
                    }
                    yield -1;
                }
                default -> -1;
            };
        }
    }

    private static class Parser {
        private final String input;
        private int pos;

        private Parser(String input) {
            this.input = input;
            this.pos = 0;
        }

        public static Node parse(String expression) {
            Parser parser = new Parser(expression);
            Node result = parser.parseExpression();
            if (parser.pos < parser.input.length()) {
                throw new IllegalArgumentException("Unexpected character at position " + parser.pos + ": " + parser.input.charAt(parser.pos));
            }
            return result;
        }

        private Node parseExpression() {
            Node left = parseTerm();
            while (true) {
                if (match('+')) {
                    left = new BinaryOpNode('+', left, parseTerm());
                } else if (match('-')) {
                    left = new BinaryOpNode('-', left, parseTerm());
                } else {
                    break;
                }
            }
            return left;
        }

        private Node parseTerm() {
            Node left = parseFactor();
            while (true) {
                if (match('*')) {
                    left = new BinaryOpNode('*', left, parseFactor());
                } else if (match('/')) {
                    left = new BinaryOpNode('/', left, parseFactor());
                } else if (hasImplicitMultiplication()) {
                    left = new BinaryOpNode('*', left, parseFactor());
                } else {
                    break;
                }
            }
            return left;
        }

        private boolean hasImplicitMultiplication() {
            if (pos >= input.length()) {
                return false;
            }
            char next = input.charAt(pos);
            return next == 'x' || next == '(' || Character.isDigit(next);
        }

        private Node parseFactor() {
            if (match('+')) {
                return parseFactor();
            }
            if (match('-')) {
                return new BinaryOpNode('*', new ConstNode(-1), parseFactor());
            }

            Node base = parseBase();
            if (match('^')) {
                Node exponent = parseFactor();
                if (!(exponent instanceof ConstNode)) {
                    throw new IllegalArgumentException("Exponent must be a constant");
                }
                double expValue = ((ConstNode) exponent).value;
                if (expValue != Math.floor(expValue)) {
                    throw new IllegalArgumentException("Exponent must be an integer");
                }
                return new BinaryOpNode('^', base, exponent);
            }
            return base;
        }

        private Node parseBase() {
            if (match('(')) {
                Node expression = parseExpression();
                if (!match(')')) {
                    throw new IllegalArgumentException("Missing closing parenthesis");
                }
                return expression;
            }
            if (peek() == 'x') {
                pos++;
                return new VarNode();
            }
            return parseNumber();
        }

        private Node parseNumber() {
            int start = pos;
            boolean hasDecimal = false;
            while (pos < input.length()) {
                char current = input.charAt(pos);
                if (Character.isDigit(current)) {
                    pos++;
                } else if (current == '.' && !hasDecimal) {
                    hasDecimal = true;
                    pos++;
                } else {
                    break;
                }
            }
            if (start == pos) {
                throw new IllegalArgumentException("Expected number at position " + pos);
            }
            double value = Double.parseDouble(input.substring(start, pos));
            return new ConstNode(value);
        }

        private char peek() {
            return pos < input.length() ? input.charAt(pos) : '\0';
        }

        private boolean match(char expected) {
            if (peek() == expected) {
                pos++;
                return true;
            }
            return false;
        }
    }
}
