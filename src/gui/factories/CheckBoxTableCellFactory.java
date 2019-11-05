package gui.factories;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

/**
 * CheckBoxTableCellFactory for the possibility to add checkboxes in table cells. 
 * @author Amrei Menzel
 *
 * @param <S>
 * @param <T>
 */
public class CheckBoxTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
	
	/**
	 * Defines the new TableCell.
	 */
    public TableCell<S, T> call(final TableColumn<S, T> param) {
        return new CheckBoxTableCell<S,T>();
    }
}