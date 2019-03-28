# Rest api :
## Coaches:
    {POST /coaches}	CoachController#createCoach
    {GET /coaches}	CoachController#findCoaches
    {GET /coaches/{coach_id}/accepted-trainings}	CoachController#countAcceptedTrainings
    {GET /coaches/{coach_id}/accepted-trainings-list}	CoachController#findAcceptedTrainings
    {GET /coaches/{coach_id}/proposed-trainings}	CoachController#countProposedTrainings
    {GET /coaches/{coach_id}/proposed-trainings-list}	CoachController#findProposedTrainings
    {GET /coaches/{coach_id}/unique-customers}	CoachController#countUniqueCustomers
    {GET /coaches/{coachId}}	CoachController#findCoach
    {DELETE /coaches/{coachId}}	CoachController#delete
## Customers:
    {GET /customers}	CustomerController#findCustomers
    {POST /customers}	CustomerController#create
    {GET /customers/{customerId}}	CustomerController#findCustomer
    {DELETE /customers/{customerId}}	CustomerController#delete
    {GET /customers/{customerId}/ask-for-contact}	CustomerController#askForContact
    {GET /customers/{customerId}/completed-trainings}	CustomerController#countCompletedTrainings
    {GET /customers/{customerId}/planned-trainings}	CustomerController#countPlannedTrainings
    {GET /customers/{customerId}/trainings}	CustomerController#findCustomerTrainings
    {GET /customers/{customerId}/unique-coaches}	CustomerController#countUniqueCoach
## Trainings:
    {POST /trainings}	TrainingController#create
    {POST /trainings/propose}	TrainingController#proposeTraining
    {GET /trainings/{training_id}}	TrainingController#findTraining
    {DELETE /trainings/{training_id}}	TrainingController#delete
    {PUT /trainings/{training_id}/accept}	TrainingController#acceptTraining
    {PUT /trainings/{training_id}/cancel}	TrainingController#cancelTraining
    {PUT /trainings/{training_id}/propose}	TrainingController#proposeNewDate
## Users:
    {POST /auth/user}   Login
    {PUT /users/set-offline}	ApplicationUserController#setOffline
    {PUT /users/set-online}	ApplicationUserController#setOnline
    {POST /users/sign-up}	ApplicationUserController#signUp
    {GET /users/{userId}}	ApplicationUserController#matchUser
