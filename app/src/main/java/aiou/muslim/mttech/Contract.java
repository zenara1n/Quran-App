package aiou.muslim.mttech;

public interface Contract {

	interface View {
		void showProgress();

		void hideProgress();

		void showError(String message);

		void showError(int resId);
	}

	interface UserActionsListener<T extends View> {

		void bindView(T view);

		void unbindView();
	}
}
