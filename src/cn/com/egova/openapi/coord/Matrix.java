package cn.com.egova.openapi.coord;

/**
 * 矩阵类，构建矩阵并提供相应操作
 * @author Administrator
 */
public class Matrix {

	protected int nNumRows;
	protected int nNumColumns;
	protected double[] data;
	
	public int getNNumColumns() {
		return nNumColumns;
	}
	
	public int getNNumRows() {
		return nNumRows;
	}
	
	public double[] getData() {
		return data;
	}
	
	/**
	 * 获取矩阵元素
	 * @param nRow
	 * @param nCol
	 * @return
	 */
	public double getElement(int nRow, int nCol){
		if(nCol >= 0 && nCol < nNumColumns && nRow >= 0 && nRow < nNumRows){
			return data[nCol + nRow * nNumColumns]; 
		} else {
			return -1;
		}
	}
	
	/**
	 * 设置行
	 * @param nRow
	 * @param data
	 */
	public void setRow(int nRow,double data[]){
		for(int j=0;j<nNumColumns;j++)
			setElement(	nRow, j, data[j]);
	}
	
	/**
	 * 设置矩阵元素
	 * @param nRow
	 * @param nCol
	 * @param value
	 * @return
	 */
	public boolean setElement(int nRow, int nCol, double value){
		if (nCol < 0 || nCol >= nNumColumns || nRow < 0 || nRow >= nNumRows)
			return false;						
		if (data == null)
			return false;
		data[nCol + nRow * nNumColumns] = value;
		return true;
	}
	
	/**
	 * 矩阵转置
	 * @return
	 */
	public Matrix T(){
		Matrix trans=new Matrix(nNumColumns, nNumRows);
		for (int i = 0 ; i < nNumRows ; ++i)
		{
			for (int j = 0 ; j < nNumColumns ; ++j)
				trans.setElement(j, i, getElement(i, j)) ;
		}
		return trans;
	}
	
	/**
	 * 构建矩阵
	 * @param nRows
	 * @param nCols
	 */
	public Matrix(int nRows, int nCols){
		nNumColumns = nRows;
		nNumRows = nCols;
		data = null;
		boolean bSuccess = init(nNumRows, nNumColumns);
	}
	
	public Matrix(){
		nNumColumns = 1;
		nNumRows = 1;
		data = null;
		boolean bSuccess = init(nNumRows, nNumColumns);
	}
	
	private boolean init(int nRows, int nCols){
		if (data!=null)
		{
			data = null;
		}

		nNumRows = nRows;
		nNumColumns = nCols;
		int nSize = nCols*nRows;
		data = new double[nSize];
		if (data == null)
			return false;					
		return true;
	}
	
	private int dcinv(double[] a,int n){
		int i,j,k,l,u,v;
		double d,p;
		int[] is=new int[n];
		int[] js=new int[n];
		for(k=0;k<=n-1;k++)
		{
			d=0;
			for(i=k;i<=n-1;i++)
				for(j=k;j<=n-1;j++)
				{
					l=i*n+j;p=Math.abs(a[l]);
					if(p>d){d=p;is[k]=i;js[k]=j;}
				}
				if(d+1==1)
				{
					System.out.println("Err**not inv\n");
					return(0);
				}
				if(is[k]!=k)
					for(j=0;j<=n-1;j++)
					{
						u=k*n+j;
						v=is[k]*n+j;
						p=a[u];
						a[u]=a[v];
						a[v]=p;
					}
					if(js[k]!=k)
						for(i=0;i<=n-1;i++)
						{
							u=i*n+k;
							v=i*n+js[k];
							p=a[u];
							a[u]=a[v];
							a[v]=p;
						}
						l=k*n+k;
						a[l]=1/a[l];
						for(j=0;j<=n-1;j++)
							if(j!=k)
							{
								u=k*n+j;
								a[u]=a[u]*a[l];
							}
							for(i=0;i<=n-1;i++)
								if(i!=k)
									for(j=0;j<=n-1;j++)
										if(j!=k)
										{
											u=i*n+j;
											a[u]=a[u]-a[i*n+k]*a[k*n+j];
										}
										for(i=0;i<=n-1;i++)
											if(i!=k)
											{
												u=i*n+k;
												a[u]=-a[u]*a[l];
											}
		}
		for(k=n-1;k>=0;k--)
		{
			if(js[k]!=k)
				for(j=0;j<=n-1;j++)
				{
					u=k*n+j;
					v=js[k]*n+j;
					p=a[u];
					a[u]=a[v];
					a[v]=p;
				}
				if(is[k]!=k)
					for(i=0;i<=n-1;i++)
					{
						u=i*n+k;
						v=i*n+is[k];
						p=a[u];
						a[u]=a[v];
						a[v]=p;
					}
	    }
		return(1);
	}
	
	public Matrix Getinv(){
		if(nNumColumns!=nNumRows)return this;
		Matrix m=new Matrix();
		m=this;
		dcinv(m.getData(),nNumColumns);
		return m;
	}
	
	/**
	 * 矩阵乘法
	 * @param a 左矩阵
	 * @param b 右矩阵
	 * @return
	 */
	public static Matrix Mup(Matrix a,Matrix b){
		if(a.getNNumColumns() != b.getNNumRows()){
			return null;
		}
		Matrix	result=new Matrix(a.getNNumRows(), b.getNNumColumns());
		double	value ;
		for (int i = 0 ; i < result.getNNumRows() ; ++i)
		{
			for (int j = 0 ; j < b.getNNumColumns() ; ++j)
			{
				value = 0.0 ;
				for (int k = 0 ; k < a.getNNumRows() ; ++k)
				{
					value += a.getElement(i, k) * b.getElement(k, j) ;
				}

				result.setElement(i, j, value) ;
			}
		}

		return result ;
	}
	
}
