create database dep6final;
use dep6final;

create table Customer(
c_Id varchar(10),
c_Name varchar(20) NOT NULL,
c_Address Text,
c_Salary DECIMAL(10,2),
constraint primary key(c_Id));

create table Item(
i_Code varchar(10),
i_Description varchar(20),
i_QtyOnHand int,
i_UnitPricce decimal(10,2),
constraint primary key(i_Code));

create table `Order`(
o_Id varchar(10),
c_Id varchar(10),
o_Date date,
o_Time time,
o_Cost decimal(10,2),
constraint primary key(o_Id),
constraint foreign key(c_Id)references Customer(c_Id) on delete cascade on update cascade);

create table OrderDetail(
o_Id varchar(10),
i_Code varchar(10),
unitPrice decimal (10,2),
qty int,
constraint primary key(o_Id,i_Code),
constraint foreign key(o_Id) references `Order`(o_Id) on delete cascade on update cascade,
constraint foreign key(i_Code) references Item(i_Code) on delete cascade on update cascade);