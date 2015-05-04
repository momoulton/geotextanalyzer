import java.util.*;

class Delimit
{
	public static void main(String[] args)
	{
		String test = "I often--nay\t always! run\n test-runs.";
		System.out.println(test);
		Scanner in = new Scanner(test).useDelimiter("-|\\s");
		while (in.hasNext())
		{
			System.out.println(in.next());
		}
	}
}
