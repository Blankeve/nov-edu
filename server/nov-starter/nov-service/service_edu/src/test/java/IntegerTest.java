public class IntegerTest {
    public static void main(String[] args) {
        Integer i = new Integer(125);
        Integer j = new Integer(125);
        Integer i2 = 129;
        Integer j2 = 129;
        int j3 = 129;
        System.out.println(i==j);
        System.out.println(i2==j2);
        System.out.println(i2==j3);
        System.out.println(i2==j);
    }
}
