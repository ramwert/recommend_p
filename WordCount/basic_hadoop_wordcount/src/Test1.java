
public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				String line = "1 2 - 1 0 - 1 9 8 4-r-5-6-3-2-4";
		 		String [] temp = line.split("-");
		 	//	String[] temp = line.split("-"); 
		 		String t1 = temp[0].trim();
		 		System.out.print(t1);
		// TODO Auto-generated method stub
		 		String temp1[] = null;
		 		int start =3;int end=7;
				  for (int i =start,k=0;i<=end;i++,k++){
					  temp1 [k]=temp[i];
				  }
				  for (String s:temp1){System.out.println(s);}
	}

}
