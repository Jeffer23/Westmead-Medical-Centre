export const session_user_key= "user";
export const session_isUserLoggedIn = "isUserLoggedIn";
export const session_user_type = "userType";
const host = "http://localhost:8080/";
export const register_service_url = host + "WestmeadMC/<USER_TYPE>/register";
export const login_service_url = host + "WestmeadMC/<USER_TYPE>/login";
export const get_non_approved_user_url = host + "WestmeadMC/<USER_TYPE>/getNonApproved";
export const approve_user_url = host + "WestmeadMC/<USER_TYPE>/approveDoctor";
export const get_approved_doctor_url = host + "WestmeadMC/doctor/getApprovedDoctors";
export const book_appointment_url = host + "WestmeadMC/patient/bookAppointment";
export const add_doctor_availability_url = host + "WestmeadMC/doctor/addAvailableTime";
export const remove_doctor_availability_url = host + "WestmeadMC/doctor/removeAvailableTime";
export const get_doctor_url = host + "WestmeadMC/doctor/getDoctor";