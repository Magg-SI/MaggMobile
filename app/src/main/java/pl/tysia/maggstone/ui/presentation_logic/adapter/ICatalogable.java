package pl.tysia.maggstone.ui.presentation_logic.adapter;

import pl.tysia.maggstone.ui.presentation_logic.filterer.IFilterable;

public interface ICatalogable extends IFilterable {
    String getTitle();
    String getShortDescription();

}
