package gui.factories;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

/**
 * ComboBoxTableCellFactory for the possibility to add comboboxes in table cells. 
 * @author Amrei Menzel
 *
 * @param <S>
 * @param <T>
 */
public class ComboBoxTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

	private ObservableList<T> nodes;

	public void setNodes(ObservableList<T> nodes) {
		this.nodes = nodes;
	}

	public ObservableList<T> getNodes() {
		return nodes;
	}

	/**
	 * Defines the new TableCell.
	 */
	@Override
	public TableCell<S, T> call(TableColumn<S, T> column) {
		if (nodes != null) {
			return new ComboBoxTableCell<S, T>(nodes);
		}
		return new ComboBoxTableCell<S, T>();
	}

}
