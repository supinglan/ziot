package pers.spl.ziot.dao;

import java.util.List;
import pers.spl.ziot.model.Device;

public interface DeviceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device
     *
     * @mbg.generated
     */
    int insert(Device record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device
     *
     * @mbg.generated
     */
    Device selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device
     *
     * @mbg.generated
     */
    List<Device> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Device record);

    List<Device> selectByOwner(int id);
}