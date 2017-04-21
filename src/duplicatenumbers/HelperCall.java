package duplicatenumbers;

public class HelperCall {

	private String ain;
	private int quantity;

	public String getAin() {
		return ain;
	}

	public void setAin(String ain) {
		this.ain = ain;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public HelperCall(String ain, int quantity) {
		super();
		this.ain = ain;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "HelperCall [ain=" + ain + ", quantity=" + quantity + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ain == null) ? 0 : ain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HelperCall other = (HelperCall) obj;
		if (ain == null) {
			if (other.ain != null)
				return false;
		} else if (!ain.equals(other.ain))
			return false;
		return true;
	}

}
