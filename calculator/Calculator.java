package ru.fizteh.fivt.students.ermolenkoevgeny.calculator;

import java.util.Stack;
import java.lang.Character;
import java.io.IOException;

public class Calculator 
{
    public static int prioritet(final char token) 
    {
        switch (token) 
        {
	        case '+':
	        case '-':
	            return 1;
            
            case '*':
            case '/':
                return 2;
        }
        return 0;
    }

    public static Integer calculate(final Integer first, final Integer second, final char operator) 
    throws IOException 
    {
        switch (operator) 
        {
	        case '+':
	            if (Math.max(first, second) > 0) 
	            {
	                if ((Integer.MAX_VALUE - first) < second)  
	                {
	                    throw new IOException("������������");
	                }
	            }   
	            else 
	            {
	                if ((Integer.MIN_VALUE + first) > second) 
	                {
	                    throw new IOException("�������");
	                }
	            }
	            return (first + second);
	            
	        case '-':
                if ((first < 0) && (first < (Integer.MIN_VALUE + second))) 
                {
                    throw new IOException("�������");
                }
                if ((second < 0) && (first > (Integer.MAX_VALUE + second))) 
                {
                    throw new IOException("������������");
                }
                return (first - second);
                
            case '*':
                if ((0 == first) | (0 == second)) 
                	return 0;
                if ((Math.abs(Integer.MAX_VALUE / first)) < Math.abs(second)) 
                {
                    throw new IOException("������������");
                }
                return (first * second);
                
            case '/':
                if (second == 0) throw new IOException("������� �� ����");
                return (first / second);
        }
        return 0;
    }


    public static void main (String args[]) 
    {
        try 
        {
            if (0 == args.length)//���� �� ������ ��������� 
            {
                System.out.println("��� ����������");
                System.exit(2);
            }
            
            StringBuilder tmp = new StringBuilder(args[0]);//������
            for (int i = 1; i < args.length; ++i) 
            {
                tmp.append(' ');//"5 +7""6-9"
                tmp.append(args[i]);
            }
            String theTask = new String(tmp);
            int radix = 18;//��������� ������� ���������
            StringBuilder theOutput = new StringBuilder();
            Stack<Character> theStack = new Stack<Character>();//���� ��� �������� � ������
            int theBegin;
            int theEnd;
            int i = 0;
            boolean open_flag = false;//������ ����� ������������� ������ �������
            boolean close_flag = false;
            int previousToken = 1;       
           
            while (i < theTask.length()) 
            {
                theBegin = i;
                //��������� ������� ���� �����
                while ((i < theTask.length()) && ((Character.isDigit(theTask.charAt(i))) | ((theTask.charAt(i) >= 'A') && (theTask.charAt(i) <= 'H') ) ) ) 
                {
                    ++i;
                }
                if (theBegin != i) 
                {
                    if (2 == previousToken) 
                    {
                        throw new IOException("�������� ����������");
                    } 
                    else 
                    {
                        theEnd = i;
                        if (!close_flag) 
                        {
                            theOutput.append(theTask.substring(theBegin, theEnd));
                            theOutput.append(' ');
                            --i;
                        } 
                        else 
                        {
                            throw new IOException("����� ������ ���� � �������");
                        }
                        if (open_flag) 
                        {
                            close_flag = true;
                        }
                        previousToken = 2;
                    }
                } 
                else 
                {
                    if ((theTask.charAt(i) == '(')) 
                    {
                        if (previousToken != 2) 
                        {
                            close_flag = false;
                            if (((i + 2) != theTask.length()) && (theTask.charAt(i + 1) == '-')) 
                            {
                                if (theTask.charAt(i + 2) == '(') 
                                {
                                    theStack.push('*');
                                    theOutput.append("-1");
                                    theOutput.append(' ');
                                    theStack.push(theTask.charAt(i));
                                    ++i;
                                }
                                else 
                                {
                                    theOutput.append('-');
                                    theStack.push(theTask.charAt(i));
                                    ++i;
                                    open_flag = true;
                                }
                            }
                            else 
                            {
                                theStack.push(theTask.charAt(i));
                            }
                        }  
                        else 
                        {
                            throw new IOException("�������� ����������");
                        }
                        previousToken = 3;
                    } 
                    else 
                    {
                        if (theTask.charAt(i) == ')') 
                        {
                            if (previousToken != 1) 
                            {
                                char top;
                                if (open_flag && !close_flag) 
                                {
                                    throw new IOException("����� ����������");
                                }
                                close_flag = false;
                                open_flag = false;
                                while ((!(theStack.empty())) && ((top = theStack.peek()) != '(')) 
                                {
                                    theOutput.append(top);
                                    theOutput.append(' ');
                                    theStack.pop();
                                }
                                if (!theStack.empty())  
                                {
                                    theStack.pop();
                                } 
                                else 
                                {
                                    throw new IOException("����� ���� ������");
                                }
                            } 
                            else 
                            {
                                throw new IOException("����� ����������");
                            }
                            previousToken = 3;
                        } 
                        else 
                        {
                            int pr = prioritet(theTask.charAt(i));
                            if (pr > 0) 
                            {
                                if (previousToken != 1) 
                                {
                                    while ((!theStack.empty()) && (pr <= (prioritet(theStack.peek())))) 
                                    {
                                        theOutput.append(theStack.pop());
                                        theOutput.append(' ');
                                    }
                                    theStack.push(theTask.charAt(i));
                                } 
                                else 
                                {
                                    throw new IOException("����� ����������");
                                }
                                previousToken = 1;
                            } 
                            else 
                            {
                                if ((theTask.charAt(i) != ' ') && (theTask.charAt(i) != '\t')) 
                                {
                                    throw new IOException("����������� ������");
                                }
                            }
                        }
                    }

                }
                ++i;
            }
            while (!theStack.empty()) 
            {
                char top = theStack.peek();
                if ((top == '(') | (top == ')')) 
                {
                    throw new IOException("����� ������ ������");
                } 
                else 
                {
                    theOutput.append(theStack.pop());
                    theOutput.append(' ');
                }
            }
            
            
            Stack<Integer> result = new Stack<Integer>();
            i = 0;
            while (i < (theOutput.length() - 1)) 
            {
                if (theOutput.charAt(i + 1) == ' ') 
                {
                    if ((Character.isDigit(theOutput.charAt(i))) | ((theOutput.charAt(i) >= 'A') && (theOutput.charAt(i) <= 'H'))) 
                    {
                        result.push(Integer.parseInt(theOutput.substring(i, i + 1), radix));
                    } 
                    else 
                    {
                        if (!result.empty()) 
                        {
                            Integer arg2 = result.pop();
                            Integer arg1;
                            if (!result.empty()) 
                            {
                                arg1 = result.pop();
                            } 
                            else 
                            {
                                throw new IOException("����� ����������");
                            }
                            result.push(calculate(arg1, arg2, theOutput.charAt(i)));
                        } 
                        else 
                        {
                            throw new IOException("����� ����������");
                        }
                    }
                    ++i;
                } 
                else 
                {
                    theBegin = i;
                    while (theOutput.charAt(i) != ' ') 
                    {
                        ++i;
                    }
                    theEnd = i;
                    String maxInteger = Integer.toString(Integer.MAX_VALUE);
                    if ((theEnd - theBegin) > maxInteger.length()) 
                    {
                        throw new IOException("������� ������� ��������");
                    }
                    if ((theEnd - theBegin) == maxInteger.length()) 
                    {
                        int k = 0;
                        for (int j = theBegin; j < theEnd; ++j, ++k)
                            if (theOutput.charAt(j) > maxInteger.charAt(k)) 
                            {
                                throw new IOException("������� ������� ��������");
                            }
                    }
                    result.push(Integer.parseInt(theOutput.substring(theBegin, theEnd), radix));
                }
                ++i;
            }
            if (result.empty()) 
            {
                throw new IOException("����� ������� �������");
            }
            int res = result.pop();
            if (result.empty()) 
            {
                System.out.println(Integer.toString(res, radix));
            } else 
            {
                throw new IOException("�������� ���������");
            }
        }
        catch(IOException e) 
        {
            System.err.println("������� ������������ ����������" + e.getMessage());
            System.exit(1);
        }
    }
}