package com.example.oohq3ebeadando.controller;
import com.example.oohq3ebeadando.Exceptions.BicycleAlreadyExistsException;
import com.example.oohq3ebeadando.Exceptions.CarAlreadyExistsException;
import com.example.oohq3ebeadando.Exceptions.RollerScooterAlreadyExistsException;
import com.example.oohq3ebeadando.Exceptions.UserAlreadyExistsException;
import com.example.oohq3ebeadando.model.*;
import com.example.oohq3ebeadando.repository.BicycleRepository;
import com.example.oohq3ebeadando.repository.CarRepository;
import com.example.oohq3ebeadando.repository.RollerScooterRepository;
import com.example.oohq3ebeadando.repository.UserRepository;
import com.example.oohq3ebeadando.service.UserService;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;
    @Autowired
    CarRepository carRepository;
    @Autowired
    BicycleRepository bicycleRepository;
    @Autowired
    RollerScooterRepository rollerScooterRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/mail")
    public String EmailPage(Model model){
        model.addAttribute("message", new Message());
        return "emailpage";
    }
    @PostMapping("/sendmail")
    public String SendMail(Model model, Message message){
        //System.out.println(message.getSubject() + " " + message.getContent());
        SimpleMailMessage tempmessage = new SimpleMailMessage();
        tempmessage.setSubject(message.getSubject());
        tempmessage.setTo("emailaddress");
        tempmessage.setText(message.getContent());
        javaMailSender.send(tempmessage);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerUserForm(Model model){
        model.addAttribute(new User());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(User user, BindingResult bindingResult, Model model){
        User tempUser = new User();

        try{
            List<User> users = userRepository.findUserByUname(user.getUname());
            if (users.size() > 0){
                throw new UserAlreadyExistsException();
            }
        }
        catch(UserAlreadyExistsException userAlreadyExistsException){
            System.err.println("User already exists with the given username! '"+user.getUname()+"'");
            return "/register";
        }

        //System.out.println(user.getUname() + " " + user.getPasswd());
        tempUser.setUname(user.getUname());
        tempUser.setPasswd(passwordEncoder.encode(user.getPasswd()));
        tempUser.setAuth("ROLE_USER");
        userService.Register(tempUser);
        return "redirect:/";
    }
    @GetMapping("/admin")
    public String adminPage(Model model, Authentication authentication){
        try {
            List<User> users = userRepository.findAll();
            users.removeIf(x-> Objects.equals(x.getUname(), authentication.getName()));

            model.addAttribute("pageTitle", "OOHQ3E - Admin Page");
            model.addAttribute("users",users);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "users";
    }
    @GetMapping("/editUser/{id}")
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("pageTitle", "OOHQ3E - Modify User");
        List<User> users = userRepository.findAll();
        //System.out.println(id);
        for (User user : users) {
            if (user.getId() == id) {
                model.addAttribute("user", user);
                return "editUser";
            }
        }
        return "not found";
    }

    @PostMapping("/editUser/{id}")
    public String updateUser(@PathVariable("id") long id, User user, BindingResult result, Model model) {
        System.out.println();
        if (result.hasErrors()) {
            return "showerror";
        }
        if (!user.getAuth().equals("ROLE_USER")  && !user.getAuth().equals("ROLE_ADMIN")){
            return "redirect:/";
        }
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++)
        {
            if (users.get(i).getId() == id) {
                User tempUser = users.get(i);

                if (!tempUser.getUname().equals(user.getUname())){
                    try{

                        List<User> tempusers = userRepository.findUserByUname(user.getUname());
                        if (tempusers.size() > 0){
                            throw new UserAlreadyExistsException();
                        }
                    }
                    catch(UserAlreadyExistsException userAlreadyExistsException){
                        System.err.println("User already exists with the given username! '"+user.getUname()+"'");
                        return "redirect:/";
                    }
                }

                tempUser.setUname(user.getUname());
                tempUser.setAuth(user.getAuth());
                try {
                    userRepository.save(tempUser);
                }catch(Exception e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
                }
                return "redirect:/";
            }
        }
        return "not found";
    }

    @GetMapping("/deleteUser/{id}")
    public String DeleteUser(@PathVariable("id")long id, Authentication authentication) {
        try {
           User userData = (userRepository.findById(id));
            if (Objects.equals(authentication.getName(), userData.getUname())){
                return "redirect:/";
            }
            if (userData != null) {
                userRepository.deleteById(id);
                return "redirect:/";
            }
            return "not found";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @RequestMapping("/cars")
    public String allCars(Model model) {
        try {
            model.addAttribute("pageTitle", "OOHQ3E - Cars");
            model.addAttribute("cars", carRepository.findAll());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "cars";
    }
    @GetMapping("/addCar")
    public String showAddAutoForm(Model model) {
        try{
            model.addAttribute("pageTitle", "OOHQ3E - Add new car");
            model.addAttribute("car", new Car());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "addCar";
    }

    @PostMapping("/addCar")
    public String saveNewCar(Car car, BindingResult bindingResult, Model model) throws CarAlreadyExistsException {
        if(bindingResult.hasErrors()) {
            return "addCar";
        }
        if (car.getHorsePower() == 0){
            return "redirect:/";
        }
        Car tempCar = new Car();
        try{
            List<Car> cars = carRepository.findCarModel(car.getModel());
            if (cars.size() > 0){
                throw new CarAlreadyExistsException();
            }
        }
        catch(CarAlreadyExistsException carAlreadyExistsException){
            System.err.println("car already exists with the given Model '"+car.getModel()+"'");
            return "addCar";
        }
        //System.out.println(car.getBrand() + " " + car.getModel() + " " + car.getHorsePower());
        tempCar.setBrand(car.getBrand());
        tempCar.setModel(car.getModel());
        tempCar.setHorsePower(car.getHorsePower());

        try {
            carRepository.save(tempCar);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "redirect:/";
    }
    @GetMapping("/updateCar/{id}")
    public String showUpdateCarForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("pageTitle", "OOHQ3E - Modify Car");
        List<Car> cars = carRepository.findAll();
        //System.out.println(id);
        for (Car car : cars) {
            if (car.getId() == id) {
                model.addAttribute("car", car);
                return "updateCar";
            }
        }
        return "not found";
    }

    @PostMapping("/updateCar/{id}")
    public String updateCar(@PathVariable("id") long id, Car car, BindingResult result, Model model) throws CarAlreadyExistsException{
        if (result.hasErrors()) {
            return "showerror";
        }
        List<Car> cars = carRepository.findAll();
        for (int i = 0; i < cars.size(); i++)
        {
            if (car.getHorsePower() <= 0){
                return "redirect:/";
            }
            if (cars.get(i).getId() == id) {
                Car tempCar = cars.get(i);

                if (!tempCar.getModel().equals(car.getModel())){
                    try{
                        List<Car> tempcars = carRepository.findCarModel(car.getModel());
                        if (tempcars.size() > 0){
                            throw new CarAlreadyExistsException();
                        }
                    }
                    catch(CarAlreadyExistsException carAlreadyExistsException){
                        System.err.println("Car already exists with the given model! '"+car.getModel()+"'");
                        return "redirect:/";
                    }
                }
                tempCar.setBrand(car.getBrand());
                tempCar.setModel(car.getModel());
                tempCar.setHorsePower(car.getHorsePower());
                try {
                    carRepository.save(tempCar);
                }catch(Exception e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
                }
                return "redirect:/";
            }
        }
        return "not found";
    }
    @GetMapping("/deleteCar/{id}")
    public String DeleteCar(@PathVariable("id")long id) {
        try {
            Optional<Car> carData = carRepository.findById(id);

            if (carData.isPresent()) {
                carRepository.deleteById(id);
                return "redirect:/";
            }
            return "not found";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }


    /*--------------------------------------------------------------------*/
    @RequestMapping("/bicycles")
    public String allBicycles(Model model) {
        try {
            model.addAttribute("pageTitle", "OOHQ3E - Bicycles");
            model.addAttribute("bicycles", bicycleRepository.findAll());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "bicycles";
    }
    @GetMapping("/addBicycle")
    public String showAddBicycleForm(Model model) {
        try{
            model.addAttribute("pageTitle", "OOHQ3E - Add new bicycle");
            model.addAttribute("bicycle", new Bicycle());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "addBicycle";
    }

    @PostMapping("/addBicycle")
    public String saveNewBicycle(Bicycle bicycle, BindingResult bindingResult, Model model) throws BicycleAlreadyExistsException {
        if(bindingResult.hasErrors()) {
            return "addBicycle";
        }
        if (bicycle.getTyreSize() <= 0){
            return "redirect:/";
        }
        Bicycle tempBicycle = new Bicycle();
        try{
            List<Bicycle> bicycles = bicycleRepository.findBicycleByType(bicycle.getType());
            if (bicycles.size() > 0){
                throw new BicycleAlreadyExistsException();
            }
        }
        catch(BicycleAlreadyExistsException bicycleAlreadyExistsException){
            System.err.println("bicycle already exists with the given type '"+bicycle.getType()+"'");
            return "addBicycle";
        }
        //System.out.println(bicycle.getBrand() + " " + car.getType() + " " + car.getTyreSize());
        tempBicycle.setBrand(bicycle.getBrand());
        tempBicycle.setType(bicycle.getType());
        tempBicycle.setColor(bicycle.getColor());
        tempBicycle.setTyreSize(bicycle.getTyreSize());

        try {
            bicycleRepository.save(tempBicycle);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "redirect:/";
    }
    @GetMapping("/updateBicycle/{id}")
    public String showBicycleUpdateForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("pageTitle", "OOHQ3E - Modify Bicycle");
        List<Bicycle> bicycles = bicycleRepository.findAll();
        //System.out.println(id);
        for (Bicycle bicycle : bicycles) {
            if (bicycle.getId() == id) {
                model.addAttribute("bicycle", bicycle);
                return "updateBicycle";
            }
        }
        return "not found";
    }

    @PostMapping("/updateBicycle/{id}")
    public String updateBicycle(@PathVariable("id") long id, Bicycle bicycle, BindingResult result, Model model) throws BicycleAlreadyExistsException{
        if (result.hasErrors()) {
            return "showerror";
        }
        List<Bicycle> bicycles = bicycleRepository.findAll();
        for (int i = 0; i < bicycles.size(); i++)
        {
            if (bicycle.getTyreSize() <= 0){
                return "redirect:/";
            }
            if (bicycles.get(i).getId() == id) {
                Bicycle tempBicycle = bicycles.get(i);

                if (!tempBicycle.getType().equals(bicycle.getType())){
                    try{
                        List<Bicycle> tempBicycles = bicycleRepository.findBicycleByType(bicycle.getType());
                        if (tempBicycles.size() > 0){
                            throw new BicycleAlreadyExistsException();
                        }
                    }
                    catch(BicycleAlreadyExistsException bicycleAlreadyExistsException){
                        System.err.println("Bicycle already exists with the given type! '"+bicycle.getType()+"'");
                        return "redirect:/";
                    }
                }

                tempBicycle.setBrand(bicycle.getBrand());
                tempBicycle.setType(bicycle.getType());
                tempBicycle.setColor(bicycle.getColor());
                tempBicycle.setTyreSize(bicycle.getTyreSize());
                try {
                    bicycleRepository.save(tempBicycle);
                }catch(Exception e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
                }
                return "redirect:/";
            }
        }
        return "not found";
    }
    @GetMapping("/deleteBicycle/{id}")
    public String DeleteBicycle(@PathVariable("id")long id) {
        try {
            Optional<Bicycle> bicycleData = bicycleRepository.findById(id);

            if (bicycleData.isPresent()) {
                bicycleRepository.deleteById(id);
                return "redirect:/";
            }
            return "not found";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }
    //--------------------------------------------------------------------------------------------------
    @RequestMapping("/rollerscooters")
    public String allRollerscooters(Model model) {
        try {
            model.addAttribute("pageTitle", "OOHQ3E - RollerScooters");
            model.addAttribute("rollerscooters", rollerScooterRepository.findAll());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "rollerscooters";
    }
    @GetMapping("/addRollerscooter")
    public String showAddRollerForm(Model model) {
        try{
            model.addAttribute("pageTitle", "OOHQ3E - Add new rollerscooter");
            model.addAttribute("rollerscooter", new RollerScooter());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "addRollerscooter";
    }

    @PostMapping("/addRollerscooter")
    public String saveNewRoller(RollerScooter rollerScooter, BindingResult bindingResult, Model model) throws RollerScooterAlreadyExistsException {
        if(bindingResult.hasErrors()) {
            return "addRollerscooter";
        }
        if (rollerScooter.getMaxSpeed() <= 0 && rollerScooter.getMaxSpeed() > 40){
            return "redirect:/";
        }
        //System.out.println(rollerScooter.getType() + "1");
        RollerScooter tempRollerscooter = new RollerScooter();
        try{
            int rollerScooters = rollerScooterRepository.findRollerScooterByType(rollerScooter.getType());
            //System.out.println(rollerScooters);
            if (rollerScooters > 0){
                //System.out.println(rollerScooter.getType() + "2");
                throw new RollerScooterAlreadyExistsException();
            }
        }
        catch(RollerScooterAlreadyExistsException rollerScooterAlreadyExistsException){
           // System.out.println(rollerScooter.getType() + "3");
            System.err.println("Rollerscooter already exists with the given Type '"+rollerScooter.getType()+"'");
            return "redirect:/";
        }

        tempRollerscooter.setBrand(rollerScooter.getBrand());
        tempRollerscooter.setType(rollerScooter.getType());
        tempRollerscooter.setColor(rollerScooter.getColor());
        tempRollerscooter.setMaxSpeed(rollerScooter.getMaxSpeed());

        try {
            rollerScooterRepository.save(tempRollerscooter);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return "redirect:/";
    }
    @GetMapping("/updateRollerscooter/{id}")
    public String showRollerUpdateForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("pageTitle", "OOHQ3E - Modify Rollerscooter");
        List<RollerScooter> rollerScooters = rollerScooterRepository.findAll();

        for (RollerScooter rollerScooter : rollerScooters) {
            if (rollerScooter.getId() == id) {
                model.addAttribute("rollerscooter", rollerScooter);
                return "updateRollerscooter";
            }
        }
        return "not found";
    }

    @PostMapping("/updateRollerscooter/{id}")
    public String updateRollerscooter(@PathVariable("id") long id, RollerScooter rollerScooter, BindingResult result, Model model) throws RollerScooterAlreadyExistsException{
        if (result.hasErrors()) {
            return "showerror";
        }
        if (rollerScooter.getMaxSpeed() <= 0 && rollerScooter.getMaxSpeed() > 40){
            return "redirect:/";
        }
        List<RollerScooter> rollerScooters = rollerScooterRepository.findAll();
        for (int i = 0; i < rollerScooters.size(); i++)
        {
            if (rollerScooter.getMaxSpeed() <= 0){
                return "redirect:/";
            }
            if (rollerScooters.get(i).getId() == id) {

                RollerScooter tempRollerScooter = rollerScooters.get(i);

                if (!tempRollerScooter.getType().equals(rollerScooter.getType())){
                    try{
                        int tempRollers = rollerScooterRepository.findRollerScooterByType(rollerScooter.getType());
                        if (tempRollers > 0){
                            throw new RollerScooterAlreadyExistsException();
                        }
                    }
                    catch(RollerScooterAlreadyExistsException rollerScooterAlreadyExistsException){
                        System.err.println("Rollerscooter already exists with the given type! '"+rollerScooter.getType()+"'");
                        return "redirect:/";
                    }
                }

                tempRollerScooter.setBrand(rollerScooter.getBrand());
                tempRollerScooter.setType(rollerScooter.getType());
                tempRollerScooter.setColor(rollerScooter.getColor());
                tempRollerScooter.setMaxSpeed(rollerScooter.getMaxSpeed());
                try {
                    rollerScooterRepository.save(tempRollerScooter);
                }catch(Exception e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
                }
                return "redirect:/";
            }
        }
        return "not found";
    }
    @GetMapping("/deleteRollerscooter/{id}")
    public String DeleteRollerscooter(@PathVariable("id")long id) {
        try {
            Optional<RollerScooter> rollerScooterData = rollerScooterRepository.findById(id);

            if (rollerScooterData.isPresent()) {
                rollerScooterRepository.deleteById(id);
                return "redirect:/";
            }
            return "not found";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }

}
