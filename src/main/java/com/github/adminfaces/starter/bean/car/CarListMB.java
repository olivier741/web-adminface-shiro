package com.github.adminfaces.starter.bean.car;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.service.CarService;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import com.github.adminfaces.persistence.model.Filter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static com.github.adminfaces.persistence.util.Messages.getMessage;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Created by rmpestano on 12/02/17.
 */
@Named
@ViewScoped
public class CarListMB extends CrudMB<Car> implements Serializable {

    @Inject
    CarService carService;

    @Inject
    @Service
    CrudService<Car, Integer> crudService; //generic injection
    
    
    Integer id;

    LazyDataModel<Car> cars;

    Filter<Car> filter = new Filter<>(new Car());

    List<Car> selectedCars; //cars selected in checkbox column

    List<Car> filteredValue;// datatable filteredValue attribute (column filters)

    @Inject
    public void initService() {
        setCrudService(carService);
    }
    
    @PostConstruct
    public void initDataModel() {
        cars = new LazyDataModel<Car>() {
            @Override
            public List<Car> load(int first, int pageSize,
                                  String sortField, SortOrder sortOrder,
                                  Map<String, Object> filters) {
                com.github.adminfaces.persistence.model.AdminSort order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? com.github.adminfaces.persistence.model.AdminSort.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? com.github.adminfaces.persistence.model.AdminSort.DESCENDING
                            : com.github.adminfaces.persistence.model.AdminSort.UNSORTED;
                }
                filter.setFirst(first)
                      .setPageSize(pageSize)
                      .setSortField(sortField)
                      .setAdminSort(order)
                      .setParams(filters);
                List<Car> list = carService.paginate(filter);
                setRowCount(carService.count(filter).intValue());
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public Car getRowData(String key) {
                return carService.findById(new Integer(key));
            }
        };
    }

       
    
     public void clear() {
        filter = new Filter<Car>(new Car());
    }

    public List<String> completeModel(String query) {
        List<String> result = carService.getModels(query);
        return result;
    }

    public void findCarById(Integer id) {
       if (id == null) {
           addDetailMessage("Provide Car ID to load");
        }if ("".equals(id)) {
           addDetailMessage("Provide Car ID to load");
        }else {
              selectedCars.add(carService.findById(id));
        }
      
    }

    public void delete() {
        int numCars = 0;
        for (Car selectedCar : selectedCars) {
            numCars++;
            carService.remove(selectedCar);
        }
        selectedCars.clear();
        addDetailMessage(numCars + " cars deleted successfully!");
    }

    public List<Car> getSelectedCars() {
        return selectedCars;
    }

    public List<Car> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Car> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public void setSelectedCars(List<Car> selectedCars) {
        this.selectedCars = selectedCars;
    }

    public LazyDataModel<Car> getCars() {
        return cars;
    }

    public void setCars(LazyDataModel<Car> cars) {
        this.cars = cars;
    }

    public Filter<Car> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Car> filter) {
        this.filter = filter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
    
   

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        String nameParam = null;
        Car carFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("name")) {
            nameParam = filter.getStringParam("name");
        } else if (has(carFilter) && carFilter.getName() != null) {
            nameParam = carFilter.getName();
        }

        if (has(nameParam)) {
            sb.append("<b>name</b>: ").append(nameParam).append(", ");
        }

        String modelParam = null;
        if (filter.hasParam("model")) {
            modelParam = filter.getStringParam("model");
        } else if (has(carFilter) && carFilter.getModel() != null) {
            modelParam = carFilter.getModel();
        }

        if (has(modelParam)) {
            sb.append("<b>model</b>: ").append(modelParam).append(", ");
        }

        Double priceParam = null;
        if (filter.hasParam("price")) {
            priceParam = filter.getDoubleParam("price");
        } else if (has(carFilter) && carFilter.getModel() != null) {
            priceParam = carFilter.getPrice();
        }

        if (has(priceParam)) {
            sb.append("<b>price</b>: ").append(priceParam).append(", ");
        }

        if (filter.hasParam("minPrice")) {
            sb.append("<b>").append(getMessage("label.minPrice")).append("</b>: ").append(filter.getParam("minPrice")).append(", ");
        }

        if (filter.hasParam("maxPrice")) {
            sb.append("<b>").append(getMessage("label.maxPrice")).append("</b>: ").append(filter.getParam("maxPrice")).append(", ");
        }

        int commaIndex = sb.lastIndexOf(",");

        if (commaIndex != -1) {
            sb.deleteCharAt(commaIndex);
        }

        if (sb.toString().trim().isEmpty()) {
            return "No search criteria";
        }

        return sb.toString();
    }

}
